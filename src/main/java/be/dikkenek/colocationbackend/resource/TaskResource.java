package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dao.TaskDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.TaskRequestDto;
import be.dikkenek.colocationbackend.dto.TaskResponseDto;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.TaskEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/task")
public class TaskResource {
    @Inject
    private TaskDao taskDao;
    @Inject
    private UserDao userDao;
    @Inject
    private FlatshareDao flatshareDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTasks() throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);

        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        List<TaskResponseDto> taskResponseDtos = flatshareEntity.getAllTasks().stream()
                .map(t -> new TaskResponseDto(
                        t.getId(),
                        t.getName(),
                        t.getDescription(),
                        t.isDone(),
                        t.getDeadline().toString(),
                        t.getRoommate().getFullName()))
                .collect(Collectors.toList());

        return Response.status(200).entity(taskResponseDtos).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTask(TaskRequestDto taskDto) throws BackendException {
        UserEntity userEntity = UserEntity.get(userDao, taskDto.getRoommateEmail());

        if (!(userEntity instanceof RoommateEntity))
            return Response.status(400).entity("Error when retrieving userEntity").build();

        RoommateEntity roommateEntity = (RoommateEntity) userEntity;
        // TaskEntity constructor automatically adds the taskEntity in the RoommateEntity's tasks list
        new TaskEntity(taskDto.getName(), taskDto.getDescription(), taskDto.isDone(), taskDto.getDeadline(), roommateEntity);

        boolean success = roommateEntity.update(userDao);
        if (success)
            return Response.status(201).build();

        return Response.status(500).entity("Error when creating task").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTask(@PathParam("id") int id) throws BackendException {
        TaskEntity taskEntity = TaskEntity.get(taskDao, id);

        if (taskEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Task not found").build();

        RoommateEntity roommateEntity = taskEntity.getRoommate();
        roommateEntity.removeTask(taskEntity);

        boolean isUpdated = roommateEntity.update(userDao);
        if (isUpdated)
            return Response.status(Response.Status.OK).build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error when deleting task").build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validateTask(@PathParam("id") int id, TaskRequestDto data) throws BackendException {
        TaskEntity taskEntity = TaskEntity.get(taskDao, id);
        if (taskEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Task not found").build();

        RoommateEntity roommateEntity = RoommateEntity.get(userDao, data.getRoommateEmail());

        if (roommateEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Roommate not found").build();

        taskEntity.setName(data.getName());
        taskEntity.setDescription(data.getDescription());
        taskEntity.setDeadline(data.getDeadline());
        taskEntity.setIsDone(data.isDone());
        taskEntity.setRoommate(roommateEntity);

        boolean isUpdated = taskEntity.update(taskDao);
        if (isUpdated)
        {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(500).entity("Error when updating task").build();
    }
}

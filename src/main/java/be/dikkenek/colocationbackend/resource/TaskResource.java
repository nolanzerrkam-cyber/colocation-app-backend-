package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.TaskDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.TaskCreateDto;
import be.dikkenek.colocationbackend.dto.TaskResponseDto;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTasks() throws BackendException {
        List<TaskEntity> tasks = TaskEntity.getAllTasks(taskDao);

        List<TaskResponseDto> tasksResponse = tasks.stream()
                .map(t -> new TaskResponseDto(t.getId(), t.getName(), t.getDescription(), t.isDone(), t.getDeadline().toString(), t.getRoommate().getFullName()))
                .collect(Collectors.toList());

        return Response.status(200).entity(tasksResponse).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTask(TaskCreateDto taskDto) throws BackendException {
        UserEntity userEntity = UserEntity.get(userDao, taskDto.getRoommateEmail());

        if (!(userEntity instanceof RoommateEntity))
            return Response.status(400).entity("Error when retrieving userEntity").build();

        RoommateEntity roommateEntity = (RoommateEntity) userEntity;
        TaskEntity taskEntity = new TaskEntity(taskDto.getName(), taskDto.getDescription(), taskDto.isDone(), taskDto.getDeadline(), roommateEntity);

        boolean success = roommateEntity.update(userDao);
        if (success)
            return Response.status(201).build();

        return Response.status(500).entity("Error when creating task").build();
    }
}

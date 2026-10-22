package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.TaskDao;
import be.dikkenek.colocationbackend.dto.TaskCreateDto;
import be.dikkenek.colocationbackend.entity.TaskEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/task")
public class TaskResource {
    @Inject
    private TaskDao taskDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTask(TaskCreateDto taskDto) throws BackendException {
        UserEntity userEntity = UserEntity.get(userDao, taskDto.getRoommateEmail());

        if(!(userEntity instanceof RoommateEntity))
            return Response.status(400).entity("Error when retrieving userEntity").build();

        TaskEntity taskEntity = new TaskEntity(taskDto.getName(), taskDto.getDescription(), taskDto.isDone(), taskDto.getDeadline(), (RoommateEntity) userEntity);

        boolean success = taskEntity.create(taskDao);
        if (success)
            return Response.status(201).build();

        return Response.status(500).entity("Error when creating task").build();
    }
}

package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.TaskDaoImpl;
import be.dikkenek.colocationbackend.entity.TaskEntity;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/task")
public class TaskResource {
    private final TaskDaoImpl taskDao = new TaskDaoImpl();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTask(TaskEntity taskEntity) {
        boolean success = taskEntity.create(taskDao);
        if (success)
            return Response.status(201).build();
        return Response.status(403).build();
    }
}

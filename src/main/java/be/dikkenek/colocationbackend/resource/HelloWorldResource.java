package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.HelloWorldDaoImpl;
import be.dikkenek.colocationbackend.entity.HelloWorldEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/test")
public class HelloWorldResource {
    @Inject
    private HelloWorldDaoImpl testDao;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(@PathParam("id") int id) {
        HelloWorldEntity testEntity = HelloWorldEntity.getById(id, testDao);

        if (testEntity == null)
            return Response.status(404).build();

        return Response.status(200).entity(testEntity).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(HelloWorldEntity testEntity) {
        boolean success = testEntity.create(testDao);
        if (success)
            return Response.status(201).build();
        return Response.status(500).build();
    }
}

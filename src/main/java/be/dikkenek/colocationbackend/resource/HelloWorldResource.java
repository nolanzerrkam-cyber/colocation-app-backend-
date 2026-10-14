package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.HelloWorldDao;
import be.dikkenek.colocationbackend.entity.HelloWorldEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/test")
public class HelloWorldResource {
    @Inject
    private HelloWorldDao helloWorldDao;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHelloWorld(@PathParam("id") int id) {
        HelloWorldEntity helloWorldEntity = HelloWorldEntity.getById(id, helloWorldDao);

        if (helloWorldEntity == null)
            return Response.status(404).build();

        return Response.status(200).entity(helloWorldEntity).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postHelloWorld(HelloWorldEntity helloWorldEntity) {
        boolean success = helloWorldEntity.create(helloWorldDao);
        if (success)
            return Response.status(201).build();
        return Response.status(500).build();
    }
}

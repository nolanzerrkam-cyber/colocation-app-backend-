package be.dikkenek.colocationbackend;

import be.dikkenek.colocationbackend.dao.Dao;
import be.dikkenek.colocationbackend.dao.TestDaoImpl;
import be.dikkenek.colocationbackend.entity.TestEntity;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/test")
public class HelloResource {
    private final Dao<TestEntity, Integer> testDao = new TestDaoImpl();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(@PathParam("id") int id) {
        Optional<TestEntity> testModelOptional = testDao.get(id);

        if (!testModelOptional.isPresent()) {
            return Response.status(404).build();
        }

        return Response.status(200).entity(testModelOptional.get()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(TestEntity testEntity) {
        boolean success = testDao.create(testEntity);
        if (success)
            return Response.status(201).build();
        return Response.status(500).build();
    }
}
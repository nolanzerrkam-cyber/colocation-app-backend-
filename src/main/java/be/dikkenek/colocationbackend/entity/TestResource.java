package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.TestDaoImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/test")
public class TestResource {
    @Inject
    private TestDaoImpl testDao;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(@PathParam("id") int id) {
        TestEntity testEntity = TestEntity.getById(id, testDao);

        if (testEntity == null)
            return Response.status(404).build();

        return Response.status(200).entity(testEntity).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(TestEntity testEntity) {
        boolean success = testEntity.create(testDao);
        if (success)
            return Response.status(201).build();
        return Response.status(500).build();
    }
}

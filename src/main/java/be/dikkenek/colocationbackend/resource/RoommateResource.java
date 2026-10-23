package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/roommate")
public class RoommateResource
{
    @Inject
    private UserDao dao;

    @GET
    @Path("/all")
    public Response getAll() throws BackendException
    {
        List<RoommateEntity> roommates = RoommateEntity.getAll(dao);

        return Response.status(Response.Status.OK)
                .entity(roommates)
                .build();
    }
}

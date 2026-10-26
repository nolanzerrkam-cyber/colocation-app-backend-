package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.RoommateResponseDto;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/roommate")
public class RoommateResource
{
    @Inject
    private UserDao dao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws BackendException {
        List<RoommateEntity> roommates = RoommateEntity.getAll(dao);

        List<RoommateResponseDto> roommateDtoList = roommates.stream().map(r -> new RoommateResponseDto(r.getEmail(), r.getFirstname(), r.getLastname(), r.getPhonenumber())).collect(Collectors.toList());

        return Response.status(Response.Status.OK)
                .entity(roommateDtoList)
                .build();
    }
}

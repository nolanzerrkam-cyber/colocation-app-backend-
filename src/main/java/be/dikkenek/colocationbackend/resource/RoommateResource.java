package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dto.RoommateResponseDto;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
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
public class RoommateResource {
    @Inject
    private FlatshareDao flatshareDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);

        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        List<RoommateResponseDto> roommateDtoList = flatshareEntity.getRoommates().stream()
                .map(r -> new RoommateResponseDto(
                        r.getEmail(),
                        r.getFirstname(),
                        r.getLastname(),
                        r.getPhonenumber(),
                        r.getFullName()))
                .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(roommateDtoList).build();
    }
}

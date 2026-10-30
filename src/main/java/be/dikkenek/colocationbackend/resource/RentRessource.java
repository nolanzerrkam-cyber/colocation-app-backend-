package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.RentDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.RentResponseDto;
import be.dikkenek.colocationbackend.dto.RentUpdateDto;
import be.dikkenek.colocationbackend.entity.RentEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/rent")
public class RentRessource
{
    @Inject
    private RentDao rentDao;

    @Inject
    private UserDao userDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRents() throws BackendException
    {
        List<RentEntity> rents = RentEntity.getAll(rentDao);

        List<RentResponseDto> rentsResponse = rents.stream().map(t -> new RentResponseDto(t.getId(), t.getPrice(), t.getLandlord().getEmail())).collect(Collectors.toList());

        return Response.status(200).entity(rentsResponse).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRent(@PathParam("id") int id, RentUpdateDto dto) throws BackendException
    {
        RentEntity rent = RentEntity.get(rentDao, id);

        if (rent == null)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("rent not found").build();
        }

        rent.setPrice(dto.getPrice());

        if (rent.update(rentDao))
        {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update the rent").build();
    }
}

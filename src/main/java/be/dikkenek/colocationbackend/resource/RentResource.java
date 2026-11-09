package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dto.RentResponseDto;
import be.dikkenek.colocationbackend.dto.RentUpdateDto;
import be.dikkenek.colocationbackend.entity.*;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rent")
public class RentResource {
    @Inject
    private FlatshareDao flatshareDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRent() throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);
        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        RentEntity rent = flatshareEntity.getRent();
        if (rent == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Rent not found").build();

        return Response.ok(new RentResponseDto(rent.getId(), rent.getAmount(), rent.getDeadlineDay())).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRent(RentUpdateDto dto) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);
        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        RentEntity rent = flatshareEntity.getRent();

        if (rent == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Rent not found").build();
        }

        rent.setAmount(dto.getAmount());
        rent.setDeadlineDay(dto.getDeadlineDay());

        if (flatshareEntity.update(flatshareDao)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("We failed to update your rent.").build();
    }
}

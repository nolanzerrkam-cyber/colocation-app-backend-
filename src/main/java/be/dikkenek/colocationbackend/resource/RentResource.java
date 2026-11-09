package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.RentResponseDto;
import be.dikkenek.colocationbackend.dto.RentUpdateDto;
import be.dikkenek.colocationbackend.entity.*;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/rent")
public class RentResource {
    @Inject
    private FlatshareDao flatshareDao;
    @Inject
    private UserDao userDao;

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

    @GET
    @Path("/isRentPaid")
    public Response isRentPaid(@QueryParam("month") Integer month, @QueryParam("year") Integer year) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);
        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        Map<String, Boolean> isRentPaidDictionary = new HashMap<>();

        flatshareEntity.getRoommates().forEach(r -> {
            isRentPaidDictionary.put(r.getEmail(), (month == null || year == null) ? r.isCurrentRentPaid() : r.isRentPaid(month, year));
        });

        return Response.status(200).entity(isRentPaidDictionary).build();
    }

    @GET
    @Path("/payRent")
    public Response isRentPaid(@QueryParam("email") String email, @QueryParam("month") Integer month, @QueryParam("year") Integer year) throws BackendException {
        if(email == null || month == null || year == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required params : email, month (1 -> 12), year").build();

        UserEntity userEntity = UserEntity.get(userDao, email);
        if (!(userEntity instanceof RoommateEntity))
            return Response.status(Response.Status.NOT_FOUND).entity("Roommate not found").build();

        RoommateEntity roommateEntity = (RoommateEntity) userEntity;
        roommateEntity.payRent(month, year);

        if(flatshareDao.update(roommateEntity.getFlatshare()))
            return Response.status(200).build();

        return Response.status(500).entity("Error when paying rent").build();
    }
}

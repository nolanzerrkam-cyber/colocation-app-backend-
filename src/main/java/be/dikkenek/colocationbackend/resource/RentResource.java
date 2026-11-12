package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dto.PayRentDto;
import be.dikkenek.colocationbackend.dto.RentResponseDto;
import be.dikkenek.colocationbackend.dto.RentRoommateDto;
import be.dikkenek.colocationbackend.dto.RentUpdateDto;
import be.dikkenek.colocationbackend.entity.*;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/rent")
public class RentResource {
    @Inject
    private FlatshareDao flatshareDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRent(@QueryParam("email") String email) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);
        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        if (email != null) {
            RoommateEntity roommateEntity = flatshareEntity.getRoommate(email);
            if (roommateEntity == null)
                return Response.status(Response.Status.NOT_FOUND).entity("Roommate not found").build();

            return Response.ok(new RentRoommateDto(roommateEntity.getRent(), roommateEntity.isCurrentRentPaid())).build();
        }

        RentEntity rent = flatshareEntity.getRent();
        if (rent == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Rent not found").build();

        return Response.ok(new RentResponseDto(rent.getId_rent(), rent.getAmount(), rent.getDeadlineDay())).build();
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

    @PUT
    @Path("/payRent")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response payRent(PayRentDto dto) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);
        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        RoommateEntity roommateEntity = flatshareEntity.getRoommate(dto.getEmail());
        if (roommateEntity == null)
            return Response.status(400).entity("Roommate not found").build();


        roommateEntity.payRent(dto.getMonth(), dto.getYear());

        if (flatshareDao.update(roommateEntity.getFlatshare()))
            return Response.status(200).build();

        return Response.status(500).entity("Error when paying rent").build();
    }
}

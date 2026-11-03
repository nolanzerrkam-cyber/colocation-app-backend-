/*
    WIP : Only one rent problem

    Since we have only one roomshare, i don't think its useful to keep the id Parameter.
    Since we have one roomshare that means we have one rent. And all the rent is divided for the number of roommates.
    I didn't think about it, but maybe then GetAll will be useless...
 */

package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.RentDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.ExpenseInsertDto;
import be.dikkenek.colocationbackend.dto.RentResponseDto;
import be.dikkenek.colocationbackend.dto.RentUpdateDto;
import be.dikkenek.colocationbackend.entity.*;
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

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRent(@PathParam("id") int id, RentUpdateDto dto) throws BackendException
    {
        RentEntity rent = RentEntity.get(rentDao, id);

        if (rent == null)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("Failed to update the rent").build();
        }

        rent.setPrice(dto.getPrice());

        if (rent.update(rentDao))
        {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update the rent").build();
    }

    /*
        This is for getting the rent of a landlord.
        To get it, we need the email of a user, that we are going to check if the userEntity is a Landlord or a Roommate.
        If the user is a Landlord, the process continues.
        Then, we are casting userEntity into a LandlordEntity, to finally get the rent.
        With that, all the information about the rent will be given.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRent(@QueryParam("email") String email) throws BackendException
    {
        // I prefer this way insted of multiple if(x == null).
        // If the email given is null, then its stays null but the twist is that if its not null, I get the userEntity since the email is not null.
        UserEntity userEntity = (email == null) ? null : UserEntity.get(userDao, email);

        if (!(userEntity instanceof LandlordEntity))
            return Response.status(403).entity("ONLY the landlord can access this method.").build();

        LandlordEntity landlordEntity = (LandlordEntity) userEntity;
        RentEntity rent = landlordEntity.getRent();

        if (rent == null)
            return Response.status(404).entity("This landlord has no rent for now.").build();

        return Response.ok(new RentResponseDto(rent.getId(), rent.getPrice(), landlordEntity.getEmail())).build();
    }
}

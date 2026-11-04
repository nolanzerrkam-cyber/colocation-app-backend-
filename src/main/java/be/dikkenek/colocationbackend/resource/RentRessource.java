package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.RentDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.RentResponseDto;
import be.dikkenek.colocationbackend.dto.RentUpdateDto;
import be.dikkenek.colocationbackend.entity.*;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rent")
public class RentRessource
{
    @Inject
    private RentDao rentDao;

    @Inject
    private UserDao userDao;

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

        if(userEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("You can't get a rent.").build();
        }

        if (!(userEntity instanceof LandlordEntity))
            return Response.status(Response.Status.FORBIDDEN).entity("You can't get a rent.").build();

        LandlordEntity landlordEntity = (LandlordEntity) userEntity;
        RentEntity rent = landlordEntity.getRent();

        if (rent == null)
            return Response.status(Response.Status.NOT_FOUND).entity("This landlord has no rent for now.").build();

        return Response.ok(new RentResponseDto(rent.getId(), rent.getPrice(), landlordEntity.getEmail())).build();
    }

    /*
        This response is to update the rent.
        Since a landlord has only one email, we just ask for the email of the user and a dto of RentUpdateDto to update the price.
        This is going to change the price of the current rent to the price of the rentUpdateDto on the parameters.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRent(@QueryParam("email") String email, RentUpdateDto dto) throws BackendException
    {
        if(dto == null)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("Updated rent is null.").build();
        }

        UserEntity userEntity = (email == null) ? null : UserEntity.get(userDao, email);

        if(userEntity == null)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("You can't get a rent.").build();
        }

        if(!(userEntity instanceof LandlordEntity))
        {
            return Response.status(Response.Status.FORBIDDEN).entity("You can't update the rent.").build();
        }

        LandlordEntity l = (LandlordEntity) userEntity;
        RentEntity rent = l.getRent();

        if(rent == null)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("This landlord has no rents.").build();
        }

        rent.setPrice(dto.getPrice());
        if(rent.update(rentDao))
        {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("We failed to update your rent.").build();
    }
}

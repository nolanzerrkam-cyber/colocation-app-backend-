package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.*;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.entity.LandlordEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
public class UserResource {
    @Inject
    private UserDao userDao;
    @Inject
    private FlatshareDao flatshareDao;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterRequestDto dto) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);

        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        UserEntity userEntity = UserEntity.get(userDao, dto.getEmail());

        if (userEntity != null)
            return Response.status(Response.Status.BAD_REQUEST).entity("User with this email address already exists").build();

        // RoommateEntity constructor automatically add the roommate in the roommates list of FlatshareEntity
        new RoommateEntity(dto.getEmail(), dto.getRawPassword(), dto.getFirstname(),
                dto.getLastname(), dto.getPhonenumber(), flatshareEntity);

        boolean isCreated = flatshareEntity.update(flatshareDao);

        if (!isCreated)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create user")
                    .build();

        return Response.status(Response.Status.CREATED)
                .build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDto loginRequestDto) throws BackendException {
        UserEntity userEntity = UserEntity.get(userDao, loginRequestDto.getEmail());

        if (userEntity == null || !userEntity.verifyPassword(loginRequestDto.getPassword()))
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid credentials")
                    .build();

        LoginResponseDto.Role role = userEntity instanceof LandlordEntity ? LoginResponseDto.Role.LANDLORD : LoginResponseDto.Role.ROOMMATE;

        LoginResponseDto loginResponseDto = new LoginResponseDto(userEntity.getEmail(), userEntity.getFirstname(),
                userEntity.getLastname(), userEntity.getPhonenumber(), role);

        return Response.status(Response.Status.OK)
                .entity(loginResponseDto)
                .build();
    }

    @DELETE
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("email") String email) throws BackendException {
        UserEntity user = UserEntity.get(userDao, email);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .build();
        }

        if (user instanceof RoommateEntity) {
            RoommateEntity roommateEntity = (RoommateEntity) user;
            FlatshareEntity flatshareEntity = roommateEntity.getFlatshare();
            flatshareEntity.removeRoommate(roommateEntity);

            if (flatshareEntity.update(flatshareDao))
                return Response.status(Response.Status.OK).build();
        } else {
            if (user.delete(userDao))
                return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Failed to delete user 3")
                .build();
    }


    // This method is only used by the roommates.
    // For this to work properly, the roommate needs to know his actual password (called oldPassword here in PasswordResetDto)
    // There is a second method for the landlord below.
    @PUT
    @Path("/newPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resetPassword(PasswordResetDTO dto) throws BackendException {
        // 1 : I have to get the userEntity, which is going to be the roommate who we change the password
        UserEntity user = UserEntity.get(userDao, dto.getEmail());
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Here, we need to be sure that the oldPassword given in the dto is not null
        // and actually matches with the actual password of the roommate.
        if (dto.getOldPassword() == null || !user.verifyPassword(dto.getOldPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Sorry your password is incorrect, please try again or ask the landlord if you forgot your password.")
                    .build();
        }

        // 2 : set the new password by updating the new one
        // setPassword will now hash the password directly.
        user.setPassword(dto.getNewPassword());
        user.update(userDao);
        return Response.status(Response.Status.OK).entity("The password has been reset !").build();
    }

    // This method will only work if we have a landlord account and his correct password
    // and if we have a roommate account that exists, with the new password.
    // For reminder, the landlord, since he has total control over the flatshare, doesn't need to have the actual password because if the password is lost, no one can reset the password.
    @PUT
    @Path("/newPassword/landlord")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resetPasswordByLandlord(LandlordResetPasswordDto dto) throws BackendException
    {
        UserEntity landlord = UserEntity.get(userDao, dto.getLandlordEmail());

        if(!(landlord instanceof LandlordEntity) || dto.getLandlordPassword() == null || !landlord.verifyPassword(dto.getLandlordPassword()))
            return Response.status(Response.Status.UNAUTHORIZED).entity("The credentials are invalid.").build();

        UserEntity roommate = UserEntity.get(userDao, dto.getRoommateEmail());

        if(roommate == null)
            return Response.status(Response.Status.NOT_FOUND).entity("The roommate you're trying to get has not been found.").build();

        if(!(roommate instanceof RoommateEntity))
            return Response.status(Response.Status.BAD_REQUEST).entity("This email is not from a roommate, try again with the correct email.").build();

        roommate.setPassword(dto.getNewPassword());
        roommate.update(userDao);
        return Response.status(Response.Status.OK).entity("Password of the roommate correctly changed !").build();
    }
}



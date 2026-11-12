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
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);
        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        RoommateEntity roommateEntity = flatshareEntity.getRoommate(dto.getEmail());
        if (roommateEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Roommate not found").build();

        if (!roommateEntity.verifyPassword(dto.getOldPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Sorry your password is incorrect, please try again or ask the landlord if you forgot your password.")
                    .build();
        }

        roommateEntity.setPassword(dto.getNewPassword());
        if (flatshareEntity.update(flatshareDao))
            return Response.status(Response.Status.OK).entity("The password has been reset !").build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("The password could not be reset").build();
    }

    // This method will only work if we have a landlord account and his correct password
    // and if we have a roommate account that exists, with the new password.
    // For reminder, the landlord, since he has total control over the flatshare, doesn't need to have the actual password because if the password is lost, no one can reset the password.
    @PUT
    @Path("/newPassword/landlord")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resetPasswordByLandlord(LandlordResetPasswordDto dto) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);
        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        LandlordEntity landlord = flatshareEntity.getLandlord();
        if (!landlord.verifyPassword(dto.getLandlordPassword()))
            return Response.status(Response.Status.UNAUTHORIZED).entity("The credentials are invalid.").build();

        RoommateEntity roommate = flatshareEntity.getRoommate(dto.getRoommateEmail());
        if (roommate == null)
            return Response.status(Response.Status.NOT_FOUND).entity("The roommate you're trying to get has not been found.").build();

        roommate.setPassword(dto.getNewPassword());

        if (flatshareEntity.update(flatshareDao))
            return Response.status(Response.Status.OK).entity("Password of the roommate correctly changed !").build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Password of the roommate could not be changed !").build();
    }
}



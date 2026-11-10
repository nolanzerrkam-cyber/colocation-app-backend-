package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginResponseDto;
import be.dikkenek.colocationbackend.dto.LoginRequestDto;
import be.dikkenek.colocationbackend.dto.RegisterRequestDto;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.entity.LandlordEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Path("/user")
public class UserResource {
    @Inject
    private UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
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

        String hashedPassword = passwordEncoder.encode(dto.getRawPassword());
        // RoommateEntity constructor automatically add the roommate in the roommates list of FlatshareEntity
        new RoommateEntity(dto.getEmail(), hashedPassword, dto.getFirstname(),
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
}
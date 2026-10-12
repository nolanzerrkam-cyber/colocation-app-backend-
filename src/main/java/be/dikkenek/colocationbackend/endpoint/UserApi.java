package be.dikkenek.colocationbackend.endpoint;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginResponseDTO;
import be.dikkenek.colocationbackend.dto.LoginRequestDTO;
import be.dikkenek.colocationbackend.dto.RegisterRequestDTO;
import be.dikkenek.colocationbackend.entity.LandlordEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
public class UserApi {
    @Inject
    private UserDao userDao;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterRequestDTO dto) {
        try {
            UserEntity entity = new RoommateEntity(dto.getEmail(), dto.getPassword(), dto.getFirstname(),
                    dto.getLastname(), dto.getPhonenumber());

            boolean isCreated = entity.create(userDao);

            if (!isCreated)
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

            return Response.status(Response.Status.CREATED)
                    .build();
        } catch (BackendException backendException) {
            return Response.status(backendException.getStatus()).entity(backendException.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequestDto) {
        try {
            UserEntity userEntity = UserEntity.get(userDao, loginRequestDto.getEmail());

            if (userEntity == null || !userEntity.verifyPassword(loginRequestDto.getPassword()))
                return Response.status(Response.Status.BAD_REQUEST)
                        .build();

            LoginResponseDTO.Role role = userEntity instanceof LandlordEntity ? LoginResponseDTO.Role.LANDLORD : LoginResponseDTO.Role.ROOMMATE;

            LoginResponseDTO loginResponseDto = new LoginResponseDTO(userEntity.getEmail(), userEntity.getFirstname(),
                    userEntity.getLastname(), userEntity.getPhonenumber(), role);

            return Response.status(Response.Status.OK)
                    .entity(loginResponseDto)
                    .build();
        } catch (BackendException backendException) {
            return Response.status(backendException.getStatus()).entity(backendException.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("email") String email) {
        try {
            UserEntity user = UserEntity.get(userDao, email);
            if (user.delete(userDao))
                return Response.status(Response.Status.OK).build();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (BackendException backendException) {
            return Response.status(backendException.getStatus()).entity(backendException.getMessage()).build();
        }
    }
}

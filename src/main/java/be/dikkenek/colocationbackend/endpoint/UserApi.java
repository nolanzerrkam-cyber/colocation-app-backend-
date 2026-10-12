package be.dikkenek.colocationbackend.endpoint;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginRegisterResponseDTO;
import be.dikkenek.colocationbackend.dto.LoginRequestDTO;
import be.dikkenek.colocationbackend.dto.RegisterRequestDTO;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
public class UserApi
{
    @Inject
    private UserDao userDao;

    @DELETE
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("email") String email) {

            UserEntity user = UserEntity.get(userDao,email);
            if(user.delete(userDao))
            {
                return Response.status(Response.Status.OK)
                        .entity("User deleted")
                        .build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("User not deleted")
                .build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterRequestDTO dto) {
        UserEntity entity = new RoommateEntity(dto.getEmail(), dto.getPassword(), dto.getFirstname(),
                dto.getLastname(), dto.getPhonenumber());

        boolean isCreated = entity.create(userDao);

        if (!isCreated)
            return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.status(Response.Status.CREATED)
                .build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequestDto) {
        UserEntity userEntity = UserEntity.get(userDao, loginRequestDto.getEmail());

        if (userEntity == null || !userEntity.verifyPassword(loginRequestDto.getPassword()))
            return Response.status(Response.Status.BAD_REQUEST)
                    .build();

        LoginRegisterResponseDTO loginResponseDto = new LoginRegisterResponseDTO(userEntity.getEmail(), userEntity.getFirstname(),
                userEntity.getLastname(), userEntity.getPhonenumber());

        return Response.status(Response.Status.OK)
                .entity(loginResponseDto)
                .build();
    }

}

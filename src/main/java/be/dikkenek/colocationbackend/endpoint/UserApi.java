package be.dikkenek.colocationbackend.endpoint;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginRegisterResponseDTO;
import be.dikkenek.colocationbackend.dto.LoginRequestDTO;
import be.dikkenek.colocationbackend.dto.RegisterRequestDTO;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
public class UserApi
{
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("colocation-backend");

    @DELETE
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("email") String email) {
            UserDao userDao = new UserDao(emf.createEntityManager());

            UserEntity user = UserEntity.get(userDao,email);
            user.delete(userDao);

            return Response.status(Response.Status.OK)
                    .entity("User deleted")
                    .build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterRequestDTO dto)
    {
        UserDao userDao = new UserDao(emf.createEntityManager());
        UserEntity entity = new RoommateEntity(dto.getEmail(),dto.getPassword(),dto.getFirstname(),
                dto.getLastname(), dto.getPhonenumber());
        UserEntity created = entity.create(userDao);
        LoginRegisterResponseDTO respDto = new LoginRegisterResponseDTO(created.getEmail(),created.getFirstname(),
                created.getLastname(), created.getPhonenumber());

        return Response.status(Response.Status.CREATED)
                .entity(respDto)
                .build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO dto)
    {
        try
        {
            UserDao dao = new UserDao(emf.createEntityManager());
            UserEntity entity = UserEntity.get(dao,dto.getEmail());

            if(!entity.verifyPassword(dto.getPassword()))
            {
                throw new IllegalArgumentException("Invalid credentials");
            }

            LoginRegisterResponseDTO respDto = new LoginRegisterResponseDTO(entity.getEmail(), entity.getFirstname(),
                    entity.getLastname(), entity.getPhonenumber());

            return Response.status(Response.Status.OK)
                    .entity(respDto)
                    .build();
        }
        catch(IllegalArgumentException e)
        {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(e.getMessage())
                    .build();
        }
        catch (RuntimeException e)
        {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

}

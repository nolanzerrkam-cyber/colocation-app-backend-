package be.dikkenek.colocationbackend.endpoint;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginRegisterResponseDTO;
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
    public Response register(UserEntity entity)
    {
        System.out.println(entity.getClass());
        UserDao userDao = new UserDao(emf.createEntityManager());
        LoginRegisterResponseDTO respDto = entity.create(userDao);
        return Response.status(Response.Status.CREATED)
                .entity(respDto)
                .build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserEntity entity)
    {
        UserDao userDao = new UserDao(emf.createEntityManager());
        LoginRegisterResponseDTO dto =  entity.login(userDao);
        return Response.status(Response.Status.CREATED)
                .entity(dto)
                .build();
    }

}

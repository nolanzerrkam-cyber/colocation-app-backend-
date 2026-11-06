package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.ShoppingItemDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.ShoppingItemGetDto;
import be.dikkenek.colocationbackend.dto.ShoppingItemPostDto;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.ShoppingItemEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/shoppingItem")
public class ShoppingItemResource
{
    @Inject
    private ShoppingItemDao itemDao;

    @Inject
    private UserDao userDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShoppingItemGetDto> getAll() throws BackendException
    {
        List<ShoppingItemEntity> entities = ShoppingItemEntity.getAll(itemDao);

        return entities.stream()
                .map(entity -> new ShoppingItemGetDto(entity.getId(), entity.getLabel(), entity.getQuantity(), entity.getRoommate().getEmail()))
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ShoppingItemPostDto dto) throws BackendException {
        RoommateEntity roommate = RoommateEntity.get(userDao,dto.getRoommateId());
        ShoppingItemEntity entity = new ShoppingItemEntity(dto.getLabel(), dto.getQuantity(), roommate);

        boolean isCreated = roommate.update(userDao);

        if (!isCreated)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create item")
                    .build();

        return Response.status(Response.Status.CREATED)
                .build();
    }
}

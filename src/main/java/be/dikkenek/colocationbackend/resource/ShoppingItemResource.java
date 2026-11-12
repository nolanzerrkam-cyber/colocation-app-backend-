package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dto.ShoppingItemGetDto;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.dto.ShoppingItemPostDto;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.ShoppingItemEntity;
import be.dikkenek.colocationbackend.entity.ShoppingQuantityEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/shoppingItem")
public class ShoppingItemResource {
    @Inject
    private FlatshareDao flatshareDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);

        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        List<ShoppingItemGetDto> shoppingItemGetDtos = flatshareEntity.getShoppingQuantityItems().stream().map(entity -> new ShoppingItemGetDto(entity.getId_quantity(), entity.getItem().getLabel(), entity.getQuantity(), entity.getItem().getRoommate().getEmail())).collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(shoppingItemGetDtos).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ShoppingItemPostDto dto) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);
        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();
        RoommateEntity roommate = flatshareEntity.getRoommate(dto.getRoommateId());
        if (roommate == null) {
            return Response.status(404).entity("Roommate not found").build();
        }
        // ShoppingItemEntity constructor automatically adds the shopping item in the flatshare shopping items list
        ShoppingItemEntity shoppingItem = new ShoppingItemEntity(dto.getLabel(), roommate);
        new ShoppingQuantityEntity(shoppingItem, dto.getQuantity(), flatshareEntity);

        boolean isCreated = flatshareEntity.update(flatshareDao);

        if (!isCreated)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create item").build();

        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id_shopItem}")
    public Response deleteShoppingItem(@PathParam("id_shopItem") int id) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);
        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        ShoppingQuantityEntity shoppingItem = flatshareEntity.getItem(id);
        if (shoppingItem == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("We couldn't find your shopping item.").build();
        }

        flatshareEntity.removeItem(shoppingItem);

        if (flatshareEntity.update(flatshareDao)) return Response.status(Response.Status.OK).build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("We failed to delete your shopping item.").build();
    }
}

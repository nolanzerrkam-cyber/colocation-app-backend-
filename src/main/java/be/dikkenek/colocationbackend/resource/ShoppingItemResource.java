package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.ShoppingItemDao;
import be.dikkenek.colocationbackend.dto.ShoppingItemGetDto;
import be.dikkenek.colocationbackend.entity.ShoppingItemEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/item")
public class ShoppingItemResource
{
    @Inject
    private ShoppingItemDao itemDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShoppingItemGetDto> getAll() throws BackendException
    {
        List<ShoppingItemEntity> entities = ShoppingItemEntity.getAll(itemDao);

        return entities.stream()
                .map(entity -> new ShoppingItemGetDto(entity.getId(), entity.getLibelle(), entity.getQuantity(), entity.getRoommate().getEmail()))
                .collect(Collectors.toList());
    }
}

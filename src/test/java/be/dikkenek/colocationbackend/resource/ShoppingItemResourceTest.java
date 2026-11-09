package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dao.ShoppingItemDao;
import be.dikkenek.colocationbackend.dto.ShoppingItemGetDto;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.ShoppingItemEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ShoppingItemResourceTest {
    @Mock
    private ShoppingItemDao itemDao;
    @Mock
    private FlatshareDao flatshareDao;
    @InjectMocks
    private ShoppingItemResource shoppingItemResource;

    private ShoppingItemEntity mockItem(int id, String label, int quantity, RoommateEntity roommate) {
        ShoppingItemEntity item = Mockito.mock(ShoppingItemEntity.class);
        Mockito.when(item.getId()).thenReturn(id);
        Mockito.when(item.getLabel()).thenReturn(label);
        Mockito.when(item.getQuantity()).thenReturn(quantity);
        Mockito.when(item.getRoommate()).thenReturn(roommate);
        return item;
    }

    @Test
    void testGetAll_Success() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommateEntity = new RoommateEntity("email@email.com","password","firstname","lastname", "0404040404", flatshareEntity);
        ShoppingItemEntity item = mockItem(1, "Lait", 2, roommateEntity);
        flatshareEntity.addRoommate(roommateEntity);
        flatshareEntity.addItem(item);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

        // When
        List<ShoppingItemGetDto> result = (List<ShoppingItemGetDto>) shoppingItemResource.getAll().getEntity();

        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals("Lait", result.get(0).getLabel());
        Assertions.assertEquals(2, result.get(0).getQuantity());
        Assertions.assertEquals("email@email.com", result.get(0).getRoommateId());
    }

    @Test
    void testGetAll_multiple_items() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate1 = new RoommateEntity("email1@email.com","password","firstname","lastname", "0404040404", flatshareEntity);
        RoommateEntity roommate2 = new RoommateEntity("email2@email.com","password","firstname","lastname", "0404040404", flatshareEntity);
        flatshareEntity.addRoommate(roommate1);
        flatshareEntity.addRoommate(roommate2);
        ShoppingItemEntity item1 = mockItem(1, "Lait", 2, roommate1);
        ShoppingItemEntity item2 = mockItem(2, "Pain", 1, roommate2);
        flatshareEntity.addItem(item1);
        flatshareEntity.addItem(item2);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

        // When
        List<ShoppingItemGetDto> result = (List<ShoppingItemGetDto>) shoppingItemResource.getAll().getEntity();

        // Then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("email1@email.com", result.get(0).getRoommateId());
        Assertions.assertEquals("email2@email.com", result.get(1).getRoommateId());
    }

    @Test
    void testGetAll_empty() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

        // When
        List<ShoppingItemGetDto> result = (List<ShoppingItemGetDto>) shoppingItemResource.getAll().getEntity();

        // Then
        Assertions.assertTrue(result.isEmpty());
    }

    /* ----------------------- /DELETE ---------------------- */

    @Test
    void deleteShopItem_nullShoppingItem() throws BackendException
    {
        // Given
        Mockito.when(itemDao.get(Mockito.any(Integer.class))).thenReturn(Optional.empty());

        // When
        Response resp = shoppingItemResource.deleteShoppingItem(10000000);

        // Then
        Assertions.assertEquals(404, resp.getStatus());
    }

    @Test
    void deleteShopItem_deleteFailed() throws BackendException
    {
        // GIVEN
        RoommateEntity rmte = new RoommateEntity("test@test.be", "password", "test", "test", "012345678");
        ShoppingItemEntity e = new ShoppingItemEntity(67, "lazagna", 1, rmte);
        Mockito.when(itemDao.get(67)).thenReturn(Optional.of(e));
        Mockito.when(itemDao.delete(Mockito.any(ShoppingItemEntity.class))).thenReturn(false);

        // WHEN
        Response resp = shoppingItemResource.deleteShoppingItem(67);

        // THEN
        Mockito.verify(itemDao).delete(e);
        Assertions.assertEquals(500, resp.getStatus());
    }

    @Test
    void deleteShopItem_Success() throws BackendException
    {
        // GIVEN
        RoommateEntity rmte = new RoommateEntity("test@test.be", "password", "test", "test", "012345678");
        ShoppingItemEntity e = new ShoppingItemEntity(67, "lazagna", 1, rmte);
        Mockito.when(itemDao.get(67)).thenReturn(Optional.of(e));
        Mockito.when(itemDao.delete(Mockito.any(ShoppingItemEntity.class))).thenReturn(true);

        // WHEN
        Response resp = shoppingItemResource.deleteShoppingItem(67);

        // THEN
        Mockito.verify(itemDao).delete(e);
        Assertions.assertEquals(200, resp.getStatus());
    }
}

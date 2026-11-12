package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.ShoppingItemGetDto;
import be.dikkenek.colocationbackend.entity.*;
import be.dikkenek.colocationbackend.dto.ShoppingItemPostDto;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ShoppingItemResourceTest {

    @Mock
    private FlatshareDao flatshareDao;
    @InjectMocks
    private ShoppingItemResource shoppingItemResource;

    @Test
    void testGetAll_Success() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommateEntity = new RoommateEntity("email@email.com", "password", "firstname", "lastname", "0404040404", flatshareEntity);
        ShoppingItemEntity item = new ShoppingItemEntity(1, "label", roommateEntity);
        ShoppingQuantityEntity itemQuantity = new ShoppingQuantityEntity(item, 10, flatshareEntity);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

        // When
        List<ShoppingItemGetDto> result = (List<ShoppingItemGetDto>) shoppingItemResource.getAll().getEntity();

        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("label", result.get(0).getLabel());
        Assertions.assertEquals(10, result.get(0).getQuantity());
        Assertions.assertEquals("email@email.com", result.get(0).getRoommateId());
    }

    @Test
    void testGetAll_multiple_items() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate1 = new RoommateEntity("email1@email.com", "password", "firstname", "lastname", "0404040404", flatshareEntity);
        RoommateEntity roommate2 = new RoommateEntity("email2@email.com", "password", "firstname", "lastname", "0404040404", flatshareEntity);
        ShoppingItemEntity item1 = new ShoppingItemEntity(1, "label", roommate1);
        ShoppingQuantityEntity itemQuantity1 = new ShoppingQuantityEntity(item1, 10, flatshareEntity);
        ShoppingItemEntity item2 = new ShoppingItemEntity(1, "label", roommate2);
        ShoppingQuantityEntity itemQuantity2 = new ShoppingQuantityEntity(item2, 10, flatshareEntity);
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

    @Test
    void shoppingItem_create_valid() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        ShoppingItemPostDto dto = new ShoppingItemPostDto("Lait", 2, "email@email.com");
        UserEntity roommate = new RoommateEntity("email@email.com", "password", "a", "a", "0472727272", flatshareEntity);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(true);

        // When
        Response response = shoppingItemResource.create(dto);

        // Then
        Assertions.assertEquals(201, response.getStatus());
        Mockito.verify(flatshareDao).update(flatshareEntity);
    }

    @Test
    void shoppingItem_create_not_valid() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        ShoppingItemPostDto dto = new ShoppingItemPostDto("Lait", 2, "email@email.com");
        UserEntity roommate = new RoommateEntity("email@email.com", "password", "a", "a", "0472727272", flatshareEntity);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(false);

        // When
        Response response = shoppingItemResource.create(dto);

        // Then
        Assertions.assertEquals(500, response.getStatus());
        Assertions.assertEquals("Failed to create item", response.getEntity());
    }

    @Test
    void shoppingItem_create_roommate_not_found() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        ShoppingItemPostDto dto = new ShoppingItemPostDto("Lait", 2, "inconnu@email.com");
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

        // When / Then
        Assertions.assertEquals(404, shoppingItemResource.create(dto).getStatus());
    }

    /* ----------------------- /DELETE ---------------------- */

    @Test
    void deleteShopItem_nullShoppingItem() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

        // When
        Response resp = shoppingItemResource.deleteShoppingItem(10000000);

        // Then
        Assertions.assertEquals(404, resp.getStatus());
    }

    @Test
    void deleteShopItem_deleteFailed() throws BackendException {
        // GIVEN
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommateEntity = new RoommateEntity("email@email.com", "password", "firstname", "lastname", "0404040404", flatshareEntity);
        ShoppingItemEntity item = new ShoppingItemEntity(1, "label", roommateEntity);
        ShoppingQuantityEntity itemQuantity = new ShoppingQuantityEntity(item, 10, flatshareEntity);

        itemQuantity.setId_quantity(67);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));
        Mockito.when(flatshareDao.update(Mockito.any())).thenReturn(false);

        // WHEN
        Response resp = shoppingItemResource.deleteShoppingItem(67);

        // THEN
        Assertions.assertEquals(500, resp.getStatus());
    }

    @Test
    void deleteShopItem_Success() throws BackendException {
        // GIVEN
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity rmte = new RoommateEntity("test@test.be", "password", "test", "test", "012345678", flatshareEntity);
        ShoppingItemEntity item = new ShoppingItemEntity(2, "label", rmte);
        ShoppingQuantityEntity itemQuantity = new ShoppingQuantityEntity(item, 10, flatshareEntity);
        itemQuantity.setId_quantity(67);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(true);

        // WHEN
        Response resp = shoppingItemResource.deleteShoppingItem(67);

        // THEN
        Assertions.assertEquals(200, resp.getStatus());
    }
}
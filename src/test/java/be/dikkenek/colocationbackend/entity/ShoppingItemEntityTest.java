package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.ShoppingItemDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingItemEntityTest {

    @Test
    void test_constructor_rejects_invalid_values() throws BackendException {
        RoommateEntity roommate = Mockito.mock(RoommateEntity.class);
        ShoppingItemEntity item = new ShoppingItemEntity(1, "Lait", 2, roommate);

        assertEquals(2, item.getQuantity());

        assertThrows(BackendException.class, () -> new ShoppingItemEntity(1, "Lait", 0, roommate));
        assertThrows(BackendException.class, () -> new ShoppingItemEntity(1, "Lait", -3, roommate));
        assertThrows(BackendException.class, () -> new ShoppingItemEntity(1, "Lait", 2, null));
    }

    @Test
    void test_setters_reject_invalid_values() throws BackendException {
        RoommateEntity roommate = Mockito.mock(RoommateEntity.class);
        ShoppingItemEntity item = new ShoppingItemEntity(1, "Lait", 2, roommate);

        assertThrows(BackendException.class, () -> item.setQuantity(0));
        assertThrows(BackendException.class, () -> item.setRoommate(null));

        assertEquals(2, item.getQuantity());
        assertSame(roommate, item.getRoommate());
    }

    @Test
    void test_getters_return_constructor_values() throws BackendException {
        RoommateEntity roommate = Mockito.mock(RoommateEntity.class);
        ShoppingItemEntity item = new ShoppingItemEntity(7, "Pain", 3, roommate);

        assertEquals(7, item.getId());
        assertEquals("Pain", item.getLabel());
        assertEquals(3, item.getQuantity());
        assertSame(roommate, item.getRoommate());
    }

    @Test
    void test_getAll_returns_dao_values() throws BackendException {
        RoommateEntity roommate = Mockito.mock(RoommateEntity.class);
        ShoppingItemDao dao = Mockito.mock(ShoppingItemDao.class);
        List<ShoppingItemEntity> items = Arrays.asList(
                new ShoppingItemEntity(1, "Lait", 2, roommate),
                new ShoppingItemEntity(2, "Pain", 1, roommate));
        Mockito.when(dao.getAll()).thenReturn(items);

        List<ShoppingItemEntity> result = ShoppingItemEntity.getAll(dao);

        assertSame(items, result);
        Mockito.verify(dao).getAll();
    }

    @Test
    void test_equals_and_hashCode_based_on_id() throws BackendException {
        RoommateEntity roommate = Mockito.mock(RoommateEntity.class);
        ShoppingItemEntity item1 = new ShoppingItemEntity(1, "Lait", 2, roommate);
        ShoppingItemEntity item2 = new ShoppingItemEntity(1, "Pain", 5, roommate);
        ShoppingItemEntity item3 = new ShoppingItemEntity(2, "Lait", 2, roommate);

        assertNotEquals(item1.hashCode(), item2.hashCode());
        assertNotEquals(item1, item2);
        assertNotEquals(item1, item3);
        assertNotEquals(null, item1);
    }

    @Test
    void test_create_valid() throws BackendException {
        // Given
        RoommateEntity roommate = Mockito.mock(RoommateEntity.class);
        ShoppingItemDao dao = Mockito.mock(ShoppingItemDao.class);
        ShoppingItemEntity item = new ShoppingItemEntity("Lait", 2, roommate);
        Mockito.when(dao.create(item)).thenReturn(true);

        // When
        boolean result = item.create(dao);

        // Then
        assertTrue(result);
        Mockito.verify(dao).create(item);
    }

    @Test
    void test_create_not_valid() throws BackendException {
        // Given
        RoommateEntity roommate = Mockito.mock(RoommateEntity.class);
        ShoppingItemDao dao = Mockito.mock(ShoppingItemDao.class);
        ShoppingItemEntity item = new ShoppingItemEntity("Lait", 2, roommate);
        Mockito.when(dao.create(item)).thenReturn(false);

        // When
        boolean result = item.create(dao);

        // Then
        assertFalse(result);
        Mockito.verify(dao).create(item);
    }

    @Test
    void test_create_dao_throws_exception() throws BackendException {
        // Given
        RoommateEntity roommate = Mockito.mock(RoommateEntity.class);
        ShoppingItemDao dao = Mockito.mock(ShoppingItemDao.class);
        ShoppingItemEntity item = new ShoppingItemEntity("Lait", 2, roommate);
        Mockito.when(dao.create(item))
                .thenThrow(new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "DB error"));

        // When / Then
        assertThrows(BackendException.class, () -> item.create(dao));
    }
}

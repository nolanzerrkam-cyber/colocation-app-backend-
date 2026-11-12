package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingItemEntityTest {

    @Test
    void test_constructor_rejects_invalid_values() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
        ShoppingItemEntity item = new ShoppingItemEntity(1, "Lait", roommate);

        assertThrows(BackendException.class, () -> new ShoppingItemEntity(1, "Lait", null));
        assertThrows(BackendException.class, () -> new ShoppingItemEntity(1, "", roommate));
        assertThrows(BackendException.class, () -> new ShoppingItemEntity(-1, "Lait", roommate));
    }

    @Test
    void test_setters_reject_invalid_values() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
        ShoppingItemEntity item = new ShoppingItemEntity(1, "Lait", roommate);

        assertThrows(BackendException.class, () -> item.setRoommate(null));
        assertThrows(BackendException.class, () -> item.setLabel(""));

        assertSame(roommate, item.getRoommate());
        assertEquals("Lait", item.getLabel());
    }

    @Test
    void test_getters_return_constructor_values() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
        ShoppingItemEntity item = new ShoppingItemEntity(7, "Pain", roommate);

        assertEquals(7, item.getId_shopping());
        assertEquals("Pain", item.getLabel());
        assertSame(roommate, item.getRoommate());
    }
}
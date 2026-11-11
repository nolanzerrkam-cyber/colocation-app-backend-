package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingItemEntityTest {

    @Test
    void test_constructor_rejects_invalid_values() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
        ShoppingItemEntity item = new ShoppingItemEntity(1, "Lait", 2, roommate);

        assertEquals(2, item.getQuantity());

        assertThrows(BackendException.class, () -> new ShoppingItemEntity(1, "Lait", 0, roommate));
        assertThrows(BackendException.class, () -> new ShoppingItemEntity(1, "Lait", -3, roommate));
        assertThrows(BackendException.class, () -> new ShoppingItemEntity(1, "Lait", 2, null));
    }

    @Test
    void test_setters_reject_invalid_values() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
        ShoppingItemEntity item = new ShoppingItemEntity(1, "Lait", 2, roommate);

        assertThrows(BackendException.class, () -> item.setQuantity(0));
        assertThrows(BackendException.class, () -> item.setRoommate(null));

        assertEquals(2, item.getQuantity());
        assertSame(roommate, item.getRoommate());
    }

//    @Test
//    void test_getters_return_constructor_values() throws BackendException {
//        FlatshareEntity flatshareEntity = new FlatshareEntity();
//        RoommateEntity roommate = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
//        ShoppingItemEntity item = new ShoppingItemEntity(7, "Pain", 3, roommate);
//
//        assertEquals(7, item.getId());
//        assertEquals("Pain", item.getLabel());
//        assertEquals(3, item.getQuantity());
//        assertSame(roommate, item.getRoommate());
//    }
}

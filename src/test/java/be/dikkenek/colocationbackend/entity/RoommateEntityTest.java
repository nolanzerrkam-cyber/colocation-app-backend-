package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class RoommateEntityTest {

    @Test
    void test_getters_return_constructor_values() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity("email1@email.com", "password", "a", "b", "0474998234", flatshareEntity);

        assertEquals("email1@email.com", roommate.getEmail());
        assertEquals("a", roommate.getFirstname());
        assertEquals("b", roommate.getLastname());
        assertEquals("0474998234", roommate.getPhonenumber());
        assertTrue(roommate.verifyPassword("password"));
        assertSame(flatshareEntity, roommate.getFlatshare());
        assertTrue(flatshareEntity.getRoommates().contains(roommate));
    }

    @Test
    void test_constructor_rejects_invalid_values() {
        FlatshareEntity flatshareEntity = new FlatshareEntity();

        assertThrows(BackendException.class, () -> new RoommateEntity("email1@email.com", "password", "a", "b", "0474998234", null));
        assertThrows(BackendException.class, () -> new RoommateEntity("email1", "password", "a", "b", "0474998234", flatshareEntity));
        assertThrows(BackendException.class, () -> new RoommateEntity("email1@email.com", "pass", "a", "b", "0474998234", flatshareEntity));
        assertThrows(BackendException.class, () -> new RoommateEntity("email1@email.com", "password", "", "b", "0474998234", flatshareEntity));
        assertThrows(BackendException.class, () -> new RoommateEntity("email1@email.com", "password", "a", "", "0474998234", flatshareEntity));
        assertThrows(BackendException.class, () -> new RoommateEntity("email1@email.com", "password", "a", "b", "", flatshareEntity));
    }

    @Test
    void test_setters() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity();

        roommate.setEmail("email2@email.com");
        roommate.setPassword("motdepasse");
        roommate.setFirstname("c");
        roommate.setLastname("d");
        roommate.setPhonenumber("0474000000");
        roommate.setFlatshare(flatshareEntity);
        roommate.setFlatshare(flatshareEntity);

        assertThrows(BackendException.class, () -> roommate.setEmail("email2"));
        assertThrows(BackendException.class, () -> roommate.setPassword("pass"));
        assertThrows(BackendException.class, () -> roommate.setFirstname(""));
        assertThrows(BackendException.class, () -> roommate.setLastname(""));
        assertThrows(BackendException.class, () -> roommate.setPhonenumber(""));
        assertThrows(BackendException.class, () -> roommate.setFlatshare(null));

        assertEquals("email2@email.com", roommate.getEmail());
        assertTrue(roommate.verifyPassword("motdepasse"));
        assertEquals("c", roommate.getFirstname());
        assertEquals("d", roommate.getLastname());
        assertEquals("0474000000", roommate.getPhonenumber());
        assertSame(flatshareEntity, roommate.getFlatshare());
    }

    @Test
    void test_pay_rent_rejects_invalid_month() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity("email1@email.com", "password", "a", "b", "0474998234", flatshareEntity);

        assertThrows(BackendException.class, () -> roommate.payRent(0, 2026));
        assertThrows(BackendException.class, () -> roommate.payRent(13, 2026));
    }

    @Test
    void test_without_expenses() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity("email1@email.com", "password", "a", "b", "0474998234", flatshareEntity);

        assertFalse(roommate.isRentPaid(1, 2026));
        assertFalse(roommate.isCurrentRentPaid());
        assertEquals(0.0f, roommate.getTotalExpenses());
    }

    @Test
    void test_get_rent() throws BackendException {
        FlatshareEntity flatshareEntity = mock(FlatshareEntity.class);
        RentEntity rent = mock(RentEntity.class);
        RoommateEntity roommate1 = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
        RoommateEntity roommate2 = new RoommateEntity("email2@email.com", "password", "b", "b", "0474998235", flatshareEntity);
        RoommateEntity roommate3 = new RoommateEntity("email3@email.com", "password", "c", "c", "0474998236", flatshareEntity);

        ArrayList<RoommateEntity> roommates = new ArrayList<>();
        roommates.add(roommate1);
        roommates.add(roommate2);
        roommates.add(roommate3);

        when(rent.getAmount()).thenReturn(900f);
        when(flatshareEntity.getRent()).thenReturn(rent);
        when(flatshareEntity.getRoommates()).thenReturn(roommates);

        assertEquals(300f, roommate1.getRent());
    }
}
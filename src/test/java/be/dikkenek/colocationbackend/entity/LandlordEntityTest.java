package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LandlordEntityTest {

    @Test
    void test_getters_return_constructor_values() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        LandlordEntity landlord = new LandlordEntity("email1@email.com", "password", "a", "b", "0474998234", flatshareEntity);

        assertEquals("email1@email.com", landlord.getEmail());
        assertEquals("a", landlord.getFirstname());
        assertEquals("b", landlord.getLastname());
        assertEquals("0474998234", landlord.getPhonenumber());
        assertTrue(landlord.verifyPassword("password"));
        assertSame(flatshareEntity, landlord.getFlatshare());
    }

    @Test
    void test_constructor_rejects_invalid_values() {
        FlatshareEntity flatshareEntity = new FlatshareEntity();

        assertThrows(BackendException.class, () -> new LandlordEntity("email1@email.com", "password", "a", "b", "0474998234", null));
        assertThrows(BackendException.class, () -> new LandlordEntity("email1", "password", "a", "b", "0474998234", flatshareEntity));
        assertThrows(BackendException.class, () -> new LandlordEntity("email1@email.com", "pass", "a", "b", "0474998234", flatshareEntity));
        assertThrows(BackendException.class, () -> new LandlordEntity("email1@email.com", "password", "", "b", "0474998234", flatshareEntity));
        assertThrows(BackendException.class, () -> new LandlordEntity("email1@email.com", "password", "a", "", "0474998234", flatshareEntity));
        assertThrows(BackendException.class, () -> new LandlordEntity("email1@email.com", "password", "a", "b", "", flatshareEntity));
    }

    @Test
    void test_set_flatshare() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        LandlordEntity landlord = new LandlordEntity();

        landlord.setFlatshare(flatshareEntity);
        assertSame(flatshareEntity, landlord.getFlatshare());
    }

    @Test
    void test_setters_reject_invalid_values() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        LandlordEntity landlord = new LandlordEntity("email1@email.com", "password", "a", "b", "0474998234", flatshareEntity);

        assertThrows(BackendException.class, () -> landlord.setFlatshare(null));

        assertSame(flatshareEntity, landlord.getFlatshare());
    }

    @Test
    void test_set_flatshare_links_landlord_to_flatshare() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        LandlordEntity landlord = new LandlordEntity("email1@email.com", "password", "a", "b", "0474998234", flatshareEntity);

        assertSame(landlord, flatshareEntity.getLandlord());
    }
}
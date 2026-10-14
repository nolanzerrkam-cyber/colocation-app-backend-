package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest
{
    @Test
    void user_set_email() throws BackendException {
        UserEntity roommateEntity = new RoommateEntity();

        roommateEntity.setEmail("test@test.com");
        assertEquals("test@test.com", roommateEntity.getEmail());

        assertThrows(BackendException.class, () -> roommateEntity.setEmail(""));
        assertThrows(BackendException.class, () -> roommateEntity.setEmail("test"));
        assertThrows(BackendException.class, () -> roommateEntity.setEmail("test@"));
        assertThrows(BackendException.class, () -> roommateEntity.setEmail("test@test"));
        assertThrows(BackendException.class, () -> roommateEntity.setEmail("test@test."));

        UserEntity landlordEntity = new LandlordEntity();

        landlordEntity.setEmail("test@test.com");
        assertEquals("test@test.com", landlordEntity.getEmail());

        assertThrows(BackendException.class, () -> landlordEntity.setEmail(""));
        assertThrows(BackendException.class, () -> landlordEntity.setEmail("test"));
        assertThrows(BackendException.class, () -> landlordEntity.setEmail("test@"));
        assertThrows(BackendException.class, () -> landlordEntity.setEmail("test@test"));
        assertThrows(BackendException.class, () -> landlordEntity.setEmail("test@test."));
    }

    @Test
    void user_set_password() throws BackendException {
        UserEntity roommateEntity = new RoommateEntity();

        roommateEntity.setPassword("abcdefghij");
        assertEquals("abcdefghij", roommateEntity.getPassword());

        assertThrows(BackendException.class, () -> roommateEntity.setPassword(""));
        assertThrows(BackendException.class, () -> roommateEntity.setPassword("aaa"));
        assertThrows(BackendException.class, () -> roommateEntity.setPassword("aaaaaaa"));

        UserEntity landlordEntity = new LandlordEntity();

        landlordEntity.setPassword("abcdefghij");
        assertEquals("abcdefghij", landlordEntity.getPassword());

        assertThrows(BackendException.class, () -> landlordEntity.setPassword(""));
        assertThrows(BackendException.class, () -> landlordEntity.setPassword("aaa"));
        assertThrows(BackendException.class, () -> landlordEntity.setPassword("aaaaaaa"));
    }

    @Test
    void user_set_firstname() throws BackendException {
        UserEntity roommateEntity = new RoommateEntity();

        roommateEntity.setFirstname("test");
        assertEquals("test", roommateEntity.getFirstname());

        assertThrows(BackendException.class, () -> roommateEntity.setFirstname(""));

        UserEntity landlordEntity = new LandlordEntity();

        landlordEntity.setFirstname("test");
        assertEquals("test", landlordEntity.getFirstname());

        assertThrows(BackendException.class, () -> landlordEntity.setFirstname(""));
    }

    @Test
    void user_set_lastname() throws BackendException {
        UserEntity roommateEntity = new RoommateEntity();

        roommateEntity.setLastname("test");
        assertEquals("test", roommateEntity.getLastname());

        assertThrows(BackendException.class, () -> roommateEntity.setLastname(""));

        UserEntity landlordEntity = new LandlordEntity();

        landlordEntity.setLastname("test");
        assertEquals("test", landlordEntity.getLastname());

        assertThrows(BackendException.class, () -> landlordEntity.setLastname(""));
    }

    @Test
    void user_set_phonenumber() throws BackendException {
        UserEntity roommateEntity = new RoommateEntity();

        roommateEntity.setPhonenumber("0472727272");
        assertEquals("0472727272", roommateEntity.getPhonenumber());

        assertThrows(BackendException.class, () -> roommateEntity.setPhonenumber(""));

        UserEntity landlordEntity = new LandlordEntity();

        landlordEntity.setPhonenumber("0472727272");
        assertEquals("0472727272", landlordEntity.getPhonenumber());

        assertThrows(BackendException.class, () -> landlordEntity.setPhonenumber(""));
    }

    @Test
    void user_verify_password() throws BackendException {
        UserEntity roommateEntity = new RoommateEntity();

        roommateEntity.setPassword("motdepasse");

        assertTrue(roommateEntity.verifyPassword("motdepasse"));
        assertFalse(roommateEntity.verifyPassword(""));

        UserEntity landlordEntity = new LandlordEntity();

        landlordEntity.setPassword("motdepasse");

        assertTrue(landlordEntity.verifyPassword("motdepasse"));
        assertFalse(landlordEntity.verifyPassword(""));
    }
}
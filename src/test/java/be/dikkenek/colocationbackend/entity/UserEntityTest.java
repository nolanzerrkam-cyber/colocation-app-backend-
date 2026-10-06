package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserEntityTest
{
    private static final String EMAIL = "john.doe@mail.com";
    private static final String PASSWORD = "secret";

    private static class TestUser extends UserEntity
    {
        TestUser() { super(); }

        TestUser(String email, String password, String firstname, String lastname, String phonenumber)
        {
            super(email, password, firstname, lastname, phonenumber);
        }
    }

    private UserDao userDao;
    private TestUser user;

    @BeforeEach
    void setUp()
    {
        userDao = mock(UserDao.class);
        user = new TestUser(EMAIL, PASSWORD, "John", "Doe", "0470123456");
    }

    @Test
    void constructor_shouldInitializeAllFields()
    {
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        assertEquals("0470123456", user.getPhonenumber());
    }

    @Test
    void defaultConstructor_shouldLeaveFieldsNull()
    {
        TestUser empty = new TestUser();

        assertNull(empty.getEmail());
        assertNull(empty.getPassword());
        assertNull(empty.getFirstname());
        assertNull(empty.getLastname());
        assertNull(empty.getPhonenumber());
    }

    @Test
    void setters_shouldUpdateFields()
    {
        TestUser empty = new TestUser();

        empty.setEmail("a@b.c");
        empty.setPassword("pwd");
        empty.setFirstname("Jane");
        empty.setLastname("Smith");
        empty.setPhonenumber("0499999999");

        assertEquals("a@b.c", empty.getEmail());
        assertEquals("pwd", empty.getPassword());
        assertEquals("Jane", empty.getFirstname());
        assertEquals("Smith", empty.getLastname());
        assertEquals("0499999999", empty.getPhonenumber());
    }

    @Test
    void create_shouldReturnSameEntity_whenEmailFreeAndCreationSucceeds()
    {
        when(userDao.get(EMAIL)).thenReturn(Optional.empty());
        when(userDao.create(user)).thenReturn(true);

        UserEntity result = user.create(userDao);

        assertSame(user, result);
        verify(userDao).create(user);
    }

    @Test
    void create_shouldThrowIllegalArgument_whenEmailAlreadyExists()
    {
        when(userDao.get(EMAIL)).thenReturn(Optional.of(new TestUser()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> user.create(userDao));

        assertEquals("Invalid email", ex.getMessage());
        verify(userDao, never()).create(any());
    }

    @Test
    void create_shouldThrowRuntime_whenDaoCreateFails()
    {
        when(userDao.get(EMAIL)).thenReturn(Optional.empty());
        when(userDao.create(user)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> user.create(userDao));

        assertEquals("Error in user creation", ex.getMessage());
    }

    @Test
    void get_shouldReturnUser_whenFound()
    {
        when(userDao.get(EMAIL)).thenReturn(Optional.of(user));

        assertSame(user, UserEntity.get(userDao, EMAIL));
    }

    @Test
    void get_shouldThrowRuntime_whenNotFound()
    {
        when(userDao.get(EMAIL)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> UserEntity.get(userDao, EMAIL));

        assertEquals("No user found", ex.getMessage());
    }

    @Test
    void getAll_shouldReturnList_whenNotEmpty()
    {
        List<UserEntity> users = new ArrayList<>(List.of(user, new TestUser()));
        when(userDao.getAll()).thenReturn(users);

        List<UserEntity> result = UserEntity.getAll(userDao);

        assertEquals(2, result.size());
        assertSame(users, result);
    }

    @Test
    void getAll_shouldThrowRuntime_whenEmpty()
    {
        when(userDao.getAll()).thenReturn(new ArrayList<>());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> UserEntity.getAll(userDao));

        assertEquals("No users found", ex.getMessage());
    }

    @Test
    void delete_shouldCallDaoWithEmail_whenSuccessful()
    {
        when(userDao.delete(EMAIL)).thenReturn(true);

        assertDoesNotThrow(() -> user.delete(userDao));

        verify(userDao).delete(EMAIL);
    }

    @Test
    void delete_shouldThrowIllegalArgument_whenDaoReturnsFalse()
    {
        when(userDao.delete(EMAIL)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> user.delete(userDao));

        assertEquals("Invalid id", ex.getMessage());
    }

    @Test
    void update_shouldCallDao_whenSuccessful()
    {
        when(userDao.update(user)).thenReturn(true);

        assertDoesNotThrow(() -> user.update(userDao));

        verify(userDao).update(user);
    }

    @Test
    void update_shouldThrowRuntime_whenDaoReturnsFalse()
    {
        when(userDao.update(user)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> user.update(userDao));

        assertEquals("Update failed", ex.getMessage());
    }

    @Test
    void verifyPassword_shouldReturnTrue_whenPasswordMatches()
    {
        assertTrue(user.verifyPassword(PASSWORD));
    }

    @Test
    void verifyPassword_shouldReturnFalse_whenPasswordDoesNotMatch()
    {
        assertFalse(user.verifyPassword("wrong"));
    }

    @Test
    void verifyPassword_shouldReturnFalse_whenGivenPasswordIsNull()
    {
        assertFalse(user.verifyPassword(null));
    }

    @Test
    void verifyPassword_shouldThrowNullPointer_whenStoredPasswordIsNull()
    {
        TestUser noPassword = new TestUser();

        assertThrows(NullPointerException.class, () -> noPassword.verifyPassword(PASSWORD));
    }
}
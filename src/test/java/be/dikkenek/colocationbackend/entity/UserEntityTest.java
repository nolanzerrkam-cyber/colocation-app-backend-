package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginRegisterResponseDTO;
import be.dikkenek.colocationbackend.entity.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityTest
{
    private static final String EMAIL = "johndoe@gmail.com";

    @Mock
    private UserDao userDao;

    private RoommateEntity buildUser()
    {
        return new RoommateEntity(EMAIL, "mdp", "john", "doe", "12345678");
    }

    private LoginRegisterResponseDTO expectedDto(UserEntity u)
    {
        return new LoginRegisterResponseDTO(u.getEmail(), u.getFirstname(), u.getLastname(), u.getPhonenumber());
    }

    private void assertSameDto(LoginRegisterResponseDTO expected, LoginRegisterResponseDTO actual)
    {
        try
        {
            for (Field f : LoginRegisterResponseDTO.class.getDeclaredFields())
            {
                if (Modifier.isStatic(f.getModifiers())) continue;
                f.setAccessible(true);
                assertEquals(f.get(expected), f.get(actual), "Champ : " + f.getName());
            }
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Test
    void emptyConstructor_fieldsAreNull()
    {
        // when
        UserEntity user = new RoommateEntity();

        // then
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getFirstname());
        assertNull(user.getLastname());
        assertNull(user.getPhonenumber());
    }

    @Test
    void fullConstructor_setsAllFields()
    {
        // when
        UserEntity user = buildUser();

        // then
        assertEquals(EMAIL, user.getEmail());
        assertEquals("mdp", user.getPassword());
        assertEquals("john", user.getFirstname());
        assertEquals("doe", user.getLastname());
        assertEquals("12345678", user.getPhonenumber());
    }

    @Test
    void setters_updateFields()
    {
        // given
        UserEntity user = new RoommateEntity();

        // when
        user.setEmail("a@b.be");
        user.setPassword("pwd");
        user.setFirstname("alice");
        user.setLastname("smith");
        user.setPhonenumber("0499999999");

        // then
        assertEquals("a@b.be", user.getEmail());
        assertEquals("pwd", user.getPassword());
        assertEquals("alice", user.getFirstname());
        assertEquals("smith", user.getLastname());
        assertEquals("0499999999", user.getPhonenumber());
    }

    @Test
    void create_success_returnsDto()
    {
        // given
        UserEntity user = buildUser();
        when(userDao.get(EMAIL)).thenReturn(Optional.empty());
        when(userDao.create(user)).thenReturn(true);

        // when
        LoginRegisterResponseDTO result = user.create(userDao);

        // then
        assertSameDto(expectedDto(user), result);
        verify(userDao, times(1)).create(user);
    }

    @Test
    void create_emailAlreadyUsed_throws()
    {
        // given
        UserEntity user = buildUser();
        when(userDao.get(EMAIL)).thenReturn(Optional.of(user));

        // when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> user.create(userDao));

        // then
        assertEquals("Invalid email", ex.getMessage());
        verify(userDao, never()).create(any());
    }

    @Test
    void create_daoFails_throws()
    {
        // given
        UserEntity user = buildUser();
        when(userDao.get(EMAIL)).thenReturn(Optional.empty());
        when(userDao.create(user)).thenReturn(false);

        // when
        RuntimeException ex = assertThrows(RuntimeException.class, () -> user.create(userDao));

        // then
        assertEquals("Error in user creation", ex.getMessage());
    }


    @Test
    void get_found_returnsUser()
    {
        // given
        UserEntity user = buildUser();
        when(userDao.get(EMAIL)).thenReturn(Optional.of(user));

        // when
        UserEntity result = UserEntity.get(userDao, EMAIL);

        // then
        assertSame(user, result);
    }

    @Test
    void get_notFound_throws()
    {
        // given
        when(userDao.get(EMAIL)).thenReturn(Optional.empty());

        // when
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UserEntity.get(userDao, EMAIL));

        // then
        assertEquals("No user found", ex.getMessage());
    }

    @Test
    void getAll_success_returnsDtos()
    {
        // given
        UserEntity u1 = buildUser();
        UserEntity u2 = new RoommateEntity("jane@doe.be", "pwd", "jane", "doe", "0480000000");
        when(userDao.getAll()).thenReturn(Arrays.asList(u1, u2));

        // when
        List<LoginRegisterResponseDTO> result = UserEntity.getAll(userDao);

        // then
        assertEquals(2, result.size());
        assertSameDto(expectedDto(u1), result.get(0));
        assertSameDto(expectedDto(u2), result.get(1));
    }

    @Test
    void getAll_empty_throws()
    {
        // given
        when(userDao.getAll()).thenReturn(Collections.emptyList());

        // when
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UserEntity.getAll(userDao));

        // then
        assertEquals("No users found", ex.getMessage());
    }


    @Test
    void login_success_returnsDto()
    {
        // given
        UserEntity user = buildUser();
        when(userDao.get(EMAIL)).thenReturn(Optional.of(buildUser()));

        // when
        LoginRegisterResponseDTO result = user.login(userDao);

        // then
        assertSameDto(expectedDto(user), result);
    }

    @Test
    void login_returnsDataFromDatabase()
    {
        // given
        UserEntity request = new RoommateEntity(EMAIL, "mdp", null, null, null);
        UserEntity inDb = buildUser();
        when(userDao.get(EMAIL)).thenReturn(Optional.of(inDb));

        // when
        LoginRegisterResponseDTO result = request.login(userDao);

        assertSameDto(expectedDto(inDb), result);
    }

    @Test
    void login_unknownEmail_throws()
    {
        // given
        UserEntity user = buildUser();
        when(userDao.get(EMAIL)).thenReturn(Optional.empty());

        // when
        RuntimeException ex = assertThrows(RuntimeException.class, () -> user.login(userDao));

        // then
        assertEquals("Invalid credentials", ex.getMessage());
    }

    @Test
    void login_wrongPassword_throws()
    {
        // given
        UserEntity user = buildUser();
        UserEntity inDb = buildUser();
        inDb.setPassword("autre");
        when(userDao.get(EMAIL)).thenReturn(Optional.of(inDb));

        // when
        SecurityException ex = assertThrows(SecurityException.class, () -> user.login(userDao));

        // then
        assertEquals("Invalid credentials", ex.getMessage());
    }

    @Test
    void delete_success()
    {
        // given
        UserEntity user = buildUser();
        when(userDao.delete(EMAIL)).thenReturn(true);

        // when
        user.delete(userDao);

        // then
        verify(userDao, times(1)).delete(EMAIL);
    }

    @Test
    void delete_daoFails_throws()
    {
        // given
        UserEntity user = buildUser();
        when(userDao.delete(EMAIL)).thenReturn(false);

        // when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> user.delete(userDao));

        // then
        assertEquals("Invalid id", ex.getMessage());
    }


    @Test
    void update_success()
    {
        // given
        UserEntity user = buildUser();
        when(userDao.update(user)).thenReturn(true);

        // when
        user.update(userDao);

        // then
        verify(userDao, times(1)).update(user);
    }

    @Test
    void update_daoFails_throws()
    {
        // given
        UserEntity user = buildUser();
        when(userDao.update(user)).thenReturn(false);

        // when
        RuntimeException ex = assertThrows(RuntimeException.class, () -> user.update(userDao));

        // then
        assertEquals("Update failed", ex.getMessage());
    }
}
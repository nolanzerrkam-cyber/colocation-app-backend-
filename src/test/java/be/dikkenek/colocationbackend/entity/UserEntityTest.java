package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginRegisterResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityTest {

    @Mock
    private UserDao userDao;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new RoommateEntity("jean@dupont.com", "mdp1234", "jean", "dupont", "1234567890");
    }

    @Test
    void create_success_shouldReturnResponseDto() {
        when(userDao.get(user.getEmail())).thenReturn(Optional.empty());
        when(userDao.create(user)).thenReturn(true);

        LoginRegisterResponseDTO dto = user.create(userDao);

        assertNotNull(dto);
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFirstname(), dto.getFirstname());
        verify(userDao).create(user);
    }

    @Test
    void create_userAlreadyExists_shouldThrowException() {
        when(userDao.get(user.getEmail())).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> user.create(userDao)
        );

        assertEquals("Invalid email", exception.getMessage());
        verify(userDao, never()).create(any());
    }

    @Test
    void create_daoFailure_shouldThrowException() {
        when(userDao.get(user.getEmail())).thenReturn(Optional.empty());
        when(userDao.create(user)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> user.create(userDao)
        );

        assertEquals("Error in user creation", exception.getMessage());
    }

    @Test
    void getStatic_found_shouldReturnEntity() {
        when(userDao.get(user.getEmail())).thenReturn(Optional.of(user));

        UserEntity result = UserEntity.get(userDao, user.getEmail());

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void getStatic_notFound_shouldThrowException() {
        when(userDao.get("unknown@email.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> UserEntity.get(userDao, "unknown@email.com")
        );

        assertEquals("No user found", exception.getMessage());
    }

    @Test
    void getAllStatic_success_shouldReturnDtoList() {
        when(userDao.getAll()).thenReturn(List.of(user));

        List<LoginRegisterResponseDTO> dtos = UserEntity.getAll(userDao);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(user.getEmail(), dtos.get(0).getEmail());
    }

    @Test
    void getAllStatic_emptyList_shouldThrowException() {
        when(userDao.getAll()).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> UserEntity.getAll(userDao)
        );

        assertEquals("No users found", exception.getMessage());
    }

    @Test
    void login_success_shouldReturnResponseDto() {
        when(userDao.get(user.getEmail())).thenReturn(Optional.of(user));

        LoginRegisterResponseDTO dto = user.login(userDao);

        assertNotNull(dto);
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void login_userNotFound_shouldThrowException() {
        when(userDao.get(user.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> user.login(userDao)
        );

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void login_wrongPassword_shouldThrowSecurityException() {
        UserEntity foundUserInDb = new RoommateEntity("jean@dupont.com", "mauvais_mdp", "jean", "dupont", "1234567890");
        when(userDao.get(user.getEmail())).thenReturn(Optional.of(foundUserInDb));

        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> user.login(userDao)
        );

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void delete_success_shouldNotThrowException() {
        when(userDao.delete(user.getEmail())).thenReturn(true);

        assertDoesNotThrow(() -> user.delete(userDao));
        verify(userDao).delete(user.getEmail());
    }

    @Test
    void delete_failure_shouldThrowException() {
        when(userDao.delete(user.getEmail())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> user.delete(userDao)
        );

        assertEquals("Invalid id", exception.getMessage());
    }

    @Test
    void update_success_shouldNotThrowException() {
        when(userDao.update(user)).thenReturn(true);

        assertDoesNotThrow(() -> user.update(userDao));
        verify(userDao).update(user);
    }

    @Test
    void update_failure_shouldThrowException() {
        when(userDao.update(user)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> user.update(userDao)
        );

        assertEquals("Update failed", exception.getMessage());
    }
}
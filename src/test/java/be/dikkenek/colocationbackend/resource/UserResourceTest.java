package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginRequestDto;
import be.dikkenek.colocationbackend.dto.LoginResponseDto;
import be.dikkenek.colocationbackend.dto.RegisterRequestDto;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserResourceTest
{
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserResource userResource;

    @Test
    void user_register_valid() throws BackendException {
        // Given
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("email@email.com", "12345678", "a", "a", "0472727272");
        Mockito.when(userDao.create(Mockito.any(UserEntity.class))).thenReturn(true);

        // When
        Response response = userResource.register(registerRequestDto);

        // Then
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    void user_register_not_valid() throws BackendException {
        // Given
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("email@email.com", "12345678", "a", "a", "0472727272");
        Mockito.when(userDao.create(Mockito.any(UserEntity.class))).thenReturn(false);

        // When
        Response response = userResource.register(registerRequestDto);

        // Then
        Assertions.assertEquals(500, response.getStatus());
    }

    @Test
    void user_register_throw() throws BackendException {
        // Given
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("email@email.com", "12345678", "a", "a", "0472727272");
        Mockito.when(userDao.create(Mockito.any(UserEntity.class))).thenThrow(new BackendException(Response.Status.BAD_REQUEST, "bad request"));

        // When
        Response response = userResource.register(registerRequestDto);

        // Then
        Assertions.assertEquals(400, response.getStatus());
        Assertions.assertEquals("bad request", response.getEntity().toString());
    }

    @Test
    void user_login_valid() throws BackendException {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto("email@email.com", "password");
        UserEntity userEntity = new RoommateEntity("email@email.com", "password", "a", "a", "0474998234");
        LoginResponseDto loginResponseDto = new LoginResponseDto(userEntity.getEmail(), userEntity.getFirstname(), userEntity.getLastname(), userEntity.getPhonenumber(), LoginResponseDto.Role.ROOMMATE);
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(userEntity));

        // When
        Response response = userResource.login(loginRequestDto);
        LoginResponseDto responseEntity = (LoginResponseDto) response.getEntity();

        // Then
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(loginResponseDto.getEmail(), responseEntity.getEmail());
        Assertions.assertEquals(loginResponseDto.getFirstname(), responseEntity.getFirstname());
        Assertions.assertEquals(loginResponseDto.getLastname(), responseEntity.getLastname());
        Assertions.assertEquals(loginResponseDto.getPhonenumber(), responseEntity.getPhonenumber());
        Assertions.assertEquals(loginResponseDto.getRole(), responseEntity.getRole());
    }

    @Test
    void user_login_not_found() throws BackendException {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto("email@email.com", "password");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.empty());

        // When
        Response response = userResource.login(loginRequestDto);

        // Then
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void user_login_passwords_dont_match() throws BackendException {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto("email@email.com", "password");
        UserEntity userEntity = new RoommateEntity("email@email.com", "passwordpassword", "a", "a", "0474998234");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(userEntity));

        // When
        Response response = userResource.login(loginRequestDto);

        // Then
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void user_login_throw() throws BackendException {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto("email@email.com", "password");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenThrow(new BackendException(Response.Status.BAD_REQUEST, "bad request"));

        // When
        Response response = userResource.login(loginRequestDto);

        // Then
        Assertions.assertEquals(400, response.getStatus());
        Assertions.assertEquals("bad request", response.getEntity().toString());
    }

    @Test
    void user_delete_valid() throws BackendException {
        // Given
        String email = "email@email.com";
        UserEntity userEntity = new RoommateEntity("email@email.com", "password", "a", "a", "0474998234");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(userEntity));
        Mockito.when(userDao.delete(Mockito.any(String.class))).thenReturn(true);

        // When
        Response response = userResource.delete(email);
        // Then
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void user_delete_not_valid() throws BackendException {
        // Given
        String email = "email@email.com";
        UserEntity userEntity = new RoommateEntity("email@email.com", "password", "a", "a", "0474998234");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(userEntity));
        Mockito.when(userDao.delete(Mockito.any(String.class))).thenReturn(false);

        // When
        Response response = userResource.delete(email);
        // Then
        Assertions.assertEquals(500, response.getStatus());
    }

    @Test
    void user_delete_throw() throws BackendException {
        // Given
        String email = "email@email.com";
        UserEntity userEntity = new RoommateEntity("email@email.com", "password", "a", "a", "0474998234");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(userEntity));
        Mockito.when(userDao.delete(Mockito.any(String.class))).thenThrow(new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "internal server error"));

        // When
        Response response = userResource.delete(email);
        // Then
        Assertions.assertEquals(500, response.getStatus());
        Assertions.assertEquals("internal server error", response.getEntity().toString());
    }
}
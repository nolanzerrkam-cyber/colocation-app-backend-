package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.*;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.entity.LandlordEntity;
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
    @Mock
    private FlatshareDao flatshareDao;
    @InjectMocks
    private UserResource userResource;

    @Test
    void user_register_valid() throws BackendException {
        // Given
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("email@email.com", "a", "a", "0472727272", "rawPassword");
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(new FlatshareEntity()));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(true);

        // When
        Response response = userResource.register(registerRequestDto);

        // Then
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    void user_register_not_valid() throws BackendException {
        // Given
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("email@email.com", "a", "a", "0472727272", "rawPassword");
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(new FlatshareEntity()));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(false);

        // When
        Response response = userResource.register(registerRequestDto);

        // Then
        Assertions.assertEquals(500, response.getStatus());
    }

    @Test
    void user_login_valid() throws BackendException {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto("email@email.com", "password");
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        UserEntity userEntity = new RoommateEntity("email@email.com", "password", "a", "a", "0474998234", flatshareEntity);
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
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        UserEntity userEntity = new RoommateEntity("email@email.com", "passwordpassword", "a", "a", "0474998234", flatshareEntity);
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(userEntity));

        // When
        Response response = userResource.login(loginRequestDto);

        // Then
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void user_delete_valid() throws BackendException {
        // Given
        String email = "email@email.com";
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        UserEntity userEntity = new RoommateEntity("email@email.com", "password", "a", "a", "0123456789", flatshareEntity);
        Mockito.when(userDao.get(Mockito.anyString())).thenReturn(Optional.of(userEntity));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(true);

        // When
        Response response = userResource.delete(email);
        // Then
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void user_delete_not_valid() throws BackendException {
        // Given
        String email = "email@email.com";
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        UserEntity userEntity = new RoommateEntity("email@email.com", "password", "a", "a", "0123456789", flatshareEntity);
        Mockito.when(userDao.get(Mockito.anyString())).thenReturn(Optional.of(userEntity));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(false);

        // When
        Response response = userResource.delete(email);
        // Then
        Assertions.assertEquals(500, response.getStatus());
    }

    @Test
    void user_reset_password_valid() throws BackendException
    {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        UserEntity roommate = new RoommateEntity("test@roommate.com", "password", "a", "b", "012345678", flatshareEntity);
        PasswordResetDTO dto = new PasswordResetDTO("newPassword123", "password", "test@roommate.com");
        Mockito.when(userDao.get("test@roommate.com")).thenReturn(Optional.of(roommate));

        // When
        Response resp = userResource.resetPassword(dto);

        // Then
        Assertions.assertEquals(200, resp.getStatus());
        Assertions.assertTrue(roommate.verifyPassword("newPassword123"));
    }

    @Test
    void user_reset_password_unknown_user() throws BackendException
    {
        PasswordResetDTO dto = new PasswordResetDTO("newPassword123", "password", "test@roommate.com");
        Mockito.when(userDao.get("test@roommate.com")).thenReturn(Optional.empty());

        Response response = userResource.resetPassword(dto);

        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    void user_reset_password_nullOldPassword() throws BackendException
    {
        FlatshareEntity flat = new FlatshareEntity();
        UserEntity roommate = new RoommateEntity("test@room.be", "oldpassword", "a", "b", "012345678", flat);
        PasswordResetDTO dto = new PasswordResetDTO("newPassword", null, "test@room.be");
        Mockito.when(userDao.get("test@room.be")).thenReturn(Optional.of(roommate));

        Response resp = userResource.resetPassword(dto);

        Assertions.assertEquals(401, resp.getStatus());
        Assertions.assertTrue(roommate.verifyPassword("oldpassword"));
    }

    @Test
    void user_reset_password_incorrectPassword() throws BackendException
    {
        FlatshareEntity flat = new FlatshareEntity();
        UserEntity roommate = new RoommateEntity("test@room.be", "password", "a", "b", "012345678", flat);
        PasswordResetDTO dto = new PasswordResetDTO("newPassword", "notSamePassword", "test@room.be");
        Mockito.when(userDao.get("test@room.be")).thenReturn(Optional.of(roommate));

        Response resp = userResource.resetPassword(dto);

        Assertions.assertEquals(401, resp.getStatus());
        Assertions.assertTrue(roommate.verifyPassword("password"));
    }

//    @Test
//    void user_reset_passwordByLandlord_valid() throws BackendException
//    {
//        FlatshareEntity flat = new FlatshareEntity();
//        UserEntity roommate = new RoommateEntity("test@room.be", "mdpofroommate", "a", "b", "012345678", flat);
//        UserEntity landlord = new LandlordEntity("test@landlord.be", "password", "a", "b", "012345678", flat);
//        LandlordResetPasswordDto dto = new LandlordResetPasswordDto("test@landlord.be", "password", "test@room.be", "aNewPassword");
//        Mockito.when(userDao.get("test@room.be")).thenReturn(Optional.of(roommate));
//        Mockito.when(userDao.get("test@landlord.be")).thenReturn(Optional.of(landlord));
//
//        Response resp = userResource.resetPasswordByLandlord(dto);
//
//        Assertions.assertEquals(200, resp.getStatus());
//        Assertions.assertTrue(roommate.verifyPassword("aNewPassword"));
//    }

    @Test
    void user_reset_passwordByLandlord_notLandlord() throws BackendException
    {
        FlatshareEntity flat = new FlatshareEntity();
        UserEntity roommate = new RoommateEntity("test@room.be", "mdpofroommate", "a", "b", "012345678", flat);
        UserEntity fakeLandlord = new RoommateEntity("test@landlord.be", "password", "a", "b", "012345678", flat);
        LandlordResetPasswordDto dto = new LandlordResetPasswordDto("test@landlord.be", "password", "test@room.be", "aNewPassword");
        Mockito.when(userDao.get("test@landlord.be")).thenReturn(Optional.of(fakeLandlord));

        Response resp = userResource.resetPasswordByLandlord(dto);

        Assertions.assertEquals(401, resp.getStatus());
        Assertions.assertTrue(roommate.verifyPassword("mdpofroommate"));
    }

//    @Test
//    void user_reset_passwordByLandlord_LandlordPasswordIsNull() throws BackendException
//    {
//        FlatshareEntity flat = new FlatshareEntity();
//        UserEntity roommate = new RoommateEntity("test@room.be", "mdpofroommate", "a", "b", "012345678", flat);
//        UserEntity landlord = new LandlordEntity("test@landlord.be", "password", "a", "b", "012345678", flat);
//        LandlordResetPasswordDto dto = new LandlordResetPasswordDto("test@landlord.be", null, "test@room.be", "aNewPassword");
//        Mockito.when(userDao.get("test@landlord.be")).thenReturn(Optional.of(landlord));
//
//        Response resp = userResource.resetPasswordByLandlord(dto);
//
//        Assertions.assertEquals(401, resp.getStatus());
//        Assertions.assertTrue(roommate.verifyPassword("mdpofroommate"));
//    }

//    @Test
//    void user_reset_passwordByLandlord_WrongPasswordLandlord() throws BackendException
//    {
//        FlatshareEntity flat = new FlatshareEntity();
//        UserEntity roommate = new RoommateEntity("test@room.be", "mdpofroommate", "a", "b", "012345678", flat);
//        UserEntity landlord = new LandlordEntity("test@landlord.be", "password", "a", "b", "012345678", flat);
//        LandlordResetPasswordDto dto = new LandlordResetPasswordDto("test@landlord.be", "wrongPassword", "test@room.be", "aNewPassword");
//        Mockito.when(userDao.get("test@landlord.be")).thenReturn(Optional.of(landlord));
//
//        Response resp = userResource.resetPasswordByLandlord(dto);
//
//        Assertions.assertEquals(401, resp.getStatus());
//        Assertions.assertTrue(roommate.verifyPassword("mdpofroommate"));
//    }

//    @Test
//    void user_reset_passwordByLandlord_RoommateNull() throws BackendException
//    {
//        FlatshareEntity flat = new FlatshareEntity();
//        UserEntity landlord = new LandlordEntity("test@landlord.be", "password", "a", "b", "012345678", flat);
//        LandlordResetPasswordDto dto = new LandlordResetPasswordDto("test@landlord.be", "password", "test@room.be", "aNewPassword");
//        Mockito.when(userDao.get("test@landlord.be")).thenReturn(Optional.of(landlord));
//        Mockito.when(userDao.get("test@room.be")).thenReturn(Optional.empty());
//
//        Response resp = userResource.resetPasswordByLandlord(dto);
//
//        Assertions.assertEquals(404, resp.getStatus());
//    }

//    @Test
//    void user_reset_passwordByLandlord_NotARoommate() throws BackendException
//    {
//        FlatshareEntity flat = new FlatshareEntity();
//        FlatshareEntity flatTwo = new FlatshareEntity();
//        UserEntity fakeRoommate = new LandlordEntity("test@room.be", "mdpofroommate", "a", "b", "012345678", flatTwo);
//        UserEntity landlord = new LandlordEntity("test@landlord.be", "password", "a", "b", "012345678", flat);
//        LandlordResetPasswordDto dto = new LandlordResetPasswordDto("test@landlord.be", "password", "test@room.be", "aNewPassword");
//        Mockito.when(userDao.get("test@room.be")).thenReturn(Optional.of(fakeRoommate));
//        Mockito.when(userDao.get("test@landlord.be")).thenReturn(Optional.of(landlord));
//
//        Response resp = userResource.resetPasswordByLandlord(dto);
//
//        Assertions.assertEquals(400, resp.getStatus());
//        Assertions.assertTrue(fakeRoommate.verifyPassword("mdpofroommate"));
//    }


}
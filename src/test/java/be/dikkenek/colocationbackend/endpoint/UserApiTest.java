package be.dikkenek.colocationbackend.endpoint;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginRegisterResponseDTO;
import be.dikkenek.colocationbackend.dto.LoginRequestDTO;
import be.dikkenek.colocationbackend.dto.RegisterRequestDTO;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserApiTest
{
    private static final String EMAIL = "john.doe@mail.com";
    private static final String PASSWORD = "secret";

    private MockedStatic<Persistence> persistenceMock;
    private MockedStatic<UserEntity> userEntityStatic;
    private MockedConstruction<UserDao> userDaoConstruction;

    private UserApi api;

    @BeforeEach
    void setUp()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        when(emf.createEntityManager()).thenReturn(em);

        persistenceMock = mockStatic(Persistence.class);
        persistenceMock.when(() -> Persistence.createEntityManagerFactory("colocation-backend"))
                .thenReturn(emf);

        userEntityStatic = mockStatic(UserEntity.class);
        userDaoConstruction = mockConstruction(UserDao.class);

        api = new UserApi();
    }

    @AfterEach
    void tearDown()
    {
        userDaoConstruction.close();
        userEntityStatic.close();
        persistenceMock.close();
    }

    private UserEntity mockUser()
    {
        UserEntity user = mock(UserEntity.class);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getFirstname()).thenReturn("John");
        when(user.getLastname()).thenReturn("Doe");
        when(user.getPhonenumber()).thenReturn("0470123456");
        return user;
    }

    @Test
    void delete_shouldReturnOk_andDeleteUser()
    {
        UserEntity user = mock(UserEntity.class);
        userEntityStatic.when(() -> UserEntity.get(any(UserDao.class), eq(EMAIL))).thenReturn(user);

        Response response = api.delete(EMAIL);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("User deleted", response.getEntity());
        verify(user).delete(any(UserDao.class));
    }

    @Test
    void delete_shouldPropagateException_whenUserNotFound()
    {
        userEntityStatic.when(() -> UserEntity.get(any(UserDao.class), eq(EMAIL)))
                .thenThrow(new RuntimeException("User not found"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> api.delete(EMAIL));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void register_shouldReturnCreated_withUserData()
    {
        RegisterRequestDTO dto = mock(RegisterRequestDTO.class);
        when(dto.getEmail()).thenReturn(EMAIL);
        when(dto.getPassword()).thenReturn(PASSWORD);
        when(dto.getFirstname()).thenReturn("John");
        when(dto.getLastname()).thenReturn("Doe");
        when(dto.getPhonenumber()).thenReturn("0470123456");

        UserEntity created = mockUser();

        try (MockedConstruction<RoommateEntity> roommateConstruction = mockConstruction(
                RoommateEntity.class,
                (mock, context) -> when(mock.create(any(UserDao.class))).thenReturn(created)))
        {
            Response response = api.register(dto);

            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertInstanceOf(LoginRegisterResponseDTO.class, response.getEntity());

            LoginRegisterResponseDTO body = (LoginRegisterResponseDTO) response.getEntity();
            assertEquals(EMAIL, body.getEmail());
            assertEquals("John", body.getFirstname());
            assertEquals("Doe", body.getLastname());
            assertEquals("0470123456", body.getPhonenumber());

            // Le RoommateEntity doit être construit avec les données du DTO
            assertEquals(1, roommateConstruction.constructed().size());
            verify(roommateConstruction.constructed().get(0)).create(any(UserDao.class));
        }
    }

    @Test
    void register_shouldPropagateException_whenCreateFails()
    {
        RegisterRequestDTO dto = mock(RegisterRequestDTO.class);
        when(dto.getEmail()).thenReturn(EMAIL);

        try (MockedConstruction<RoommateEntity> ignored = mockConstruction(
                RoommateEntity.class,
                (mock, context) -> when(mock.create(any(UserDao.class)))
                        .thenThrow(new IllegalStateException("Email already used"))))
        {
            assertThrows(IllegalStateException.class, () -> api.register(dto));
        }
    }

    @Test
    void login_shouldReturnOk_whenCredentialsAreValid()
    {
        LoginRequestDTO dto = mock(LoginRequestDTO.class);
        when(dto.getEmail()).thenReturn(EMAIL);
        when(dto.getPassword()).thenReturn(PASSWORD);

        UserEntity user = mockUser();
        when(user.verifyPassword(PASSWORD)).thenReturn(true);
        userEntityStatic.when(() -> UserEntity.get(any(UserDao.class), eq(EMAIL))).thenReturn(user);

        Response response = api.login(dto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        LoginRegisterResponseDTO body = (LoginRegisterResponseDTO) response.getEntity();
        assertEquals(EMAIL, body.getEmail());
        assertEquals("John", body.getFirstname());
        assertEquals("Doe", body.getLastname());
        assertEquals("0470123456", body.getPhonenumber());
    }

    @Test
    void login_shouldReturnUnauthorized_whenPasswordIsInvalid()
    {
        LoginRequestDTO dto = mock(LoginRequestDTO.class);
        when(dto.getEmail()).thenReturn(EMAIL);
        when(dto.getPassword()).thenReturn("wrong");

        UserEntity user = mockUser();
        when(user.verifyPassword("wrong")).thenReturn(false);
        userEntityStatic.when(() -> UserEntity.get(any(UserDao.class), eq(EMAIL))).thenReturn(user);

        Response response = api.login(dto);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Invalid credentials", response.getEntity());
    }

    @Test
    void login_shouldReturnUnauthorized_whenGetThrowsIllegalArgumentException()
    {
        LoginRequestDTO dto = mock(LoginRequestDTO.class);
        when(dto.getEmail()).thenReturn(EMAIL);
        when(dto.getPassword()).thenReturn(PASSWORD);

        userEntityStatic.when(() -> UserEntity.get(any(UserDao.class), eq(EMAIL)))
                .thenThrow(new IllegalArgumentException("Bad email"));

        Response response = api.login(dto);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Bad email", response.getEntity());
    }

    @Test
    void login_shouldReturnNotFound_whenUserDoesNotExist()
    {
        LoginRequestDTO dto = mock(LoginRequestDTO.class);
        when(dto.getEmail()).thenReturn(EMAIL);
        when(dto.getPassword()).thenReturn(PASSWORD);

        userEntityStatic.when(() -> UserEntity.get(any(UserDao.class), eq(EMAIL)))
                .thenThrow(new RuntimeException("User not found"));

        Response response = api.login(dto);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("User not found", response.getEntity());
    }
}
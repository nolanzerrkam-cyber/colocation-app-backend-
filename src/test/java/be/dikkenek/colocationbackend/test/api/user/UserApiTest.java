package be.dikkenek.colocationbackend.test.api.user;

import be.dikkenek.colocationbackend.dao.*;
import be.dikkenek.colocationbackend.dto.LoginRegisterResponseDTO;
import be.dikkenek.colocationbackend.endpoint.UserApi;
import be.dikkenek.colocationbackend.entity.*;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApiTest
{
    private static final String EMAIL = "johndoe@gmail.com";

    @Mock
    private EntityManagerFactory emf;
    @Mock
    private EntityManager em;

    private MockedStatic<Persistence> persistenceMock;
    private MockedConstruction<UserDao> daoConstruction;

    private Consumer<UserDao> daoSetup = dao -> {};

    private UserApi api;

    @BeforeEach
    void setUp()
    {
        when(emf.createEntityManager()).thenReturn(em);

        persistenceMock = Mockito.mockStatic(Persistence.class);
        persistenceMock.when(() -> Persistence.createEntityManagerFactory("colocation-backend")).thenReturn(emf);

        daoConstruction = Mockito.mockConstruction(UserDao.class, (dao, context) -> daoSetup.accept(dao));

        api = new UserApi();
    }

    @AfterEach
    void tearDown()
    {
        daoConstruction.close();
        persistenceMock.close();
    }

    private UserDao constructedDao()
    {
        return daoConstruction.constructed().get(0);
    }

    @Test
    void delete_returnsOk()
    {
        UserEntity user = mock(RoommateEntity.class);
        daoSetup = dao -> when(dao.get(EMAIL)).thenReturn(Optional.of(user));

        Response response = api.delete(EMAIL);

        assertEquals(200, response.getStatus());
        assertEquals("User deleted", response.getEntity());
        verify(user, times(1)).delete(constructedDao());
    }

    @Test
    void delete_userNotFound_throws()
    {
        daoSetup = dao -> when(dao.get(EMAIL)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> api.delete(EMAIL));

        assertEquals("No user found", ex.getMessage());
    }

    @Test
    void delete_deleteFails_throws()
    {
        UserEntity user = mock(RoommateEntity.class);
        daoSetup = dao -> when(dao.get(EMAIL)).thenReturn(Optional.of(user));
        doThrow(new IllegalArgumentException("Invalid id")).when(user).delete(any(UserDao.class));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> api.delete(EMAIL));

        assertEquals("Invalid id", ex.getMessage());
    }


    @Test
    void register_returnsCreated()
    {
        LoginRegisterResponseDTO dto = new LoginRegisterResponseDTO(EMAIL, "John", "Doe", "12345678");
        UserEntity entity = mock(RoommateEntity.class);
        when(entity.create(any(UserDao.class))).thenReturn(dto);

        Response response = api.register(entity);

        assertEquals(201, response.getStatus());
        assertSame(dto, response.getEntity());
        verify(entity, times(1)).create(constructedDao());
    }

    @Test
    void register_emailAlreadyUsed_throws()
    {
        UserEntity entity = mock(RoommateEntity.class);
        when(entity.create(any(UserDao.class))).thenThrow(new IllegalArgumentException("Invalid email"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> api.register(entity));

        assertEquals("Invalid email", ex.getMessage());
    }

    @Test
    void login_returnsDto()
    {
        LoginRegisterResponseDTO dto = new LoginRegisterResponseDTO(EMAIL, "John", "Doe", "12345678");
        UserEntity entity = mock(RoommateEntity.class);
        when(entity.login(any(UserDao.class))).thenReturn(dto);

        Response response = api.login(entity);

        assertEquals(201, response.getStatus());
        assertSame(dto, response.getEntity());
        verify(entity, times(1)).login(constructedDao());
    }

    @Test
    void login_invalidCredentials_throws()
    {
        UserEntity entity = mock(RoommateEntity.class);
        when(entity.login(any(UserDao.class))).thenThrow(new SecurityException("Invalid credentials"));

        SecurityException ex = assertThrows(SecurityException.class, () -> api.login(entity));

        assertEquals("Invalid credentials", ex.getMessage());
    }
}
package be.dikkenek.colocationbackend.endpoint;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginRegisterResponseDTO;
import be.dikkenek.colocationbackend.entity.UserEntity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApiTest {

    private UserApi userApi;

    @Mock
    private UserEntity userEntityMock;

    @Mock
    private LoginRegisterResponseDTO responseDtoMock;

    @Mock
    private EntityManagerFactory emfMock;

    private MockedStatic persistenceMockedStatic;
    private MockedStatic userEntityMockedStatic;
    private MockedConstruction userDaoMockedConstruction;

    @BeforeEach
    void setUp() {
        userApi = new UserApi();

        persistenceMockedStatic = mockStatic(Persistence.class);
        persistenceMockedStatic.when(() -> Persistence.createEntityManagerFactory(anyString())).thenReturn(emfMock);
        userDaoMockedConstruction = mockConstruction(UserDao.class);
    }

    @AfterEach
    void tearDown() {
        if (persistenceMockedStatic != null) persistenceMockedStatic.close();
        if (userDaoMockedConstruction != null) userDaoMockedConstruction.close();
        if (userEntityMockedStatic != null) userEntityMockedStatic.close();
    }

    @Test
    void delete_shouldReturnOkResponse() {
        userEntityMockedStatic = mockStatic(UserEntity.class);
        String email = "jean@dupont.com";

        userEntityMockedStatic.when(() -> UserEntity.get(any(UserDao.class), eq(email))).thenReturn(userEntityMock);
        doNothing().when(userEntityMock).delete(any(UserDao.class));

        Response response = userApi.delete(email);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("User deleted", response.getEntity());
    }

    @Test
    void register_shouldReturnCreatedResponse() {
        when(userEntityMock.create(any(UserDao.class))).thenReturn(responseDtoMock);

        Response response = userApi.register(userEntityMock);

        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseDtoMock, response.getEntity());
    }

    @Test
    void login_shouldReturnCreatedResponse() {
        when(userEntityMock.login(any(UserDao.class))).thenReturn(responseDtoMock);

        Response response = userApi.login(userEntityMock);

        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseDtoMock, response.getEntity());
    }
}
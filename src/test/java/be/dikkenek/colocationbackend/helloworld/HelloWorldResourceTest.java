package be.dikkenek.colocationbackend.helloworld;

import be.dikkenek.colocationbackend.dao.HelloWorldDaoImpl;
import be.dikkenek.colocationbackend.entity.HelloWorldEntity;
import be.dikkenek.colocationbackend.resource.HelloWorldResource;
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
public class HelloWorldResourceTest {
    @Mock
    private HelloWorldDaoImpl helloWorldDao;
    @InjectMocks
    private HelloWorldResource helloWorldResource;

    @Test
    void test_get_helloworld_found() {
        // Given
        HelloWorldEntity inputHelloWorld = new HelloWorldEntity(-1, "Test attribute...");
        Mockito.when(helloWorldDao.get(Mockito.any(Integer.class))).thenReturn(Optional.of(inputHelloWorld));

        // When
        Response response = helloWorldResource.getHelloWorld(-1);

        // 3. Then
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("Test attribute...", ((HelloWorldEntity) response.getEntity()).getTestAttribute());

        // Check dao create method has been called 1 time only
        Mockito.verify(helloWorldDao, Mockito.times(1)).get(-1);
    }

    @Test
    void test_get_helloworld_not_found() {
        // Given
        Mockito.when(helloWorldDao.get(Mockito.any(Integer.class))).thenReturn(Optional.empty());

        // When
        Response response = helloWorldResource.getHelloWorld(-1);

        // 3. Then
        Assertions.assertEquals(404, response.getStatus());

        // Check dao create method has been called 1 time only
        Mockito.verify(helloWorldDao, Mockito.times(1)).get(-1);
    }
}

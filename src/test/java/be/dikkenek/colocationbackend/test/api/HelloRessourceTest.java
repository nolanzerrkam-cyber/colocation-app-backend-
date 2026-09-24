package be.dikkenek.colocationbackend.test.api;

import be.dikkenek.colocationbackend.HelloResource;
import be.dikkenek.colocationbackend.entity.TestEntity;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloResourceTest extends JerseyTest {
   /* @Override
    protected Application configure() {
        return new ResourceConfig(HelloResource.class)
                .register(JacksonFeature.class);
    }

    @Override
    protected void configureClient(org.glassfish.jersey.client.ClientConfig config) {
        config.register(JacksonFeature.class);
    }

    @Test
    void get_test() {
        TestEntity t = new TestEntity("Test...");
        target("/test")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(t, MediaType.APPLICATION_JSON));

        Response response = target("/test/2").request(MediaType.TEXT_PLAIN).get();
        String message = response.readEntity(String.class);

        assertEquals(200, response.getStatus());
        assertEquals("Test...", message);
    }*/
}
package com.istasse.hello_world.test.api;

import com.istasse.hello_world.HelloResource;
import com.istasse.hello_world.entity.Text;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(HelloResource.class)
                .register(JacksonFeature.class);
    }

    @Override
    protected void configureClient(org.glassfish.jersey.client.ClientConfig config) {
        config.register(JacksonFeature.class);
    }

    @Test
    void get_devrait_retourner_le_message() {
        Text t = new Text("Hello world");
        target("/hello-world")
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.entity(t, MediaType.APPLICATION_JSON));

        Response response = target("/hello-world/1").request(MediaType.TEXT_PLAIN).get();
        String message = response.readEntity(String.class);

        System.out.println(message);

        assertEquals(200, response.getStatus());
        assertEquals("Hello world", message);
    }


}
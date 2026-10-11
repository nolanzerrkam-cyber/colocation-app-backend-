package be.dikkenek.colocationbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

// With provider, jersey will automatically detect this class, and use it for every json conversion
// I was forced to do this, because postman always send me back server internal errors.
@Provider
public class JacksonConfigLocalDateTime implements ContextResolver<ObjectMapper>
{
    private final ObjectMapper objectMapper;

    public JacksonConfigLocalDateTime()
    {
        // ObjectMapper is the class, from jackkson, that convert json into java objects, and vice versa
        objectMapper = new ObjectMapper();
        // This will make Jackson dosen't read LocalDateTime. With this,
        // it will solve the problem when I send a api/expense on postman.
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public ObjectMapper getContext(Class<?> type)
    {
        return objectMapper;
    }
}

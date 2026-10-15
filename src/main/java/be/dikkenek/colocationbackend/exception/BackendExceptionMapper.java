package be.dikkenek.colocationbackend.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BackendExceptionMapper implements ExceptionMapper<BackendException> {
    @Override
    public Response toResponse(BackendException backendException) {
        backendException.printStackTrace();
        return Response.status(backendException.getStatus()).entity(backendException.getMessage()).build();
    }
}

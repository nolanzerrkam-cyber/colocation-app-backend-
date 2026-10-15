package be.dikkenek.colocationbackend.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidFormatExceptionMapper implements ExceptionMapper<InvalidFormatException> {
    @Override
    public Response toResponse(InvalidFormatException exception) {
        exception.printStackTrace();
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad request: invalid format").build();
    }
}

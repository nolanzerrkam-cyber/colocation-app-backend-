package be.dikkenek.colocationbackend.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException>
{
    @Override
    public Response toResponse(NullPointerException error)
    {
        return Response.status(Response.Status.BAD_REQUEST).entity("The body of your request is null.").build();
    }
}

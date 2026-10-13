package be.dikkenek.colocationbackend.exception;

import jakarta.ws.rs.core.Response;

public class BackendException extends Exception {
    private Response.Status status;
    private String errorMessage;

    public BackendException(Response.Status status, String errorMessage) {
        super(errorMessage);
        this.status = status;
    }

    public Response.Status getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

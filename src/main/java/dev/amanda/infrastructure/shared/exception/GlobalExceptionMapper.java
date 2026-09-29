package dev.amanda.infrastructure.shared.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.java.Log;

// implements ExceptionMapper<BaseApiException>

@Provider
@Log
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        System.out.println(exception.getCause());
        if (exception instanceof BaseApiException e) {
            return Response
                    .status(e.getApiError().getStatus())
                    .entity(new ErrorResponse(e.getApiError(), e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (exception instanceof WebApplicationException e) {
            return Response
                    .status(400)
                    .entity(new ErrorResponse(ApiError.GENERIC_BAD_REQUEST, e.getMessage()))
                    .build();
        }

        log.severe(exception.getMessage());

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(ApiError.INTERNAL_SERVER_ERROR))
                .build();
    }
}

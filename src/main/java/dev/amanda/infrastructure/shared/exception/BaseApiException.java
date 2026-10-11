package dev.amanda.infrastructure.shared.exception;

import lombok.Getter;

@Getter
public class BaseApiException extends RuntimeException {
    private final ApiError apiError;
    private final String formattedMessage;

    public BaseApiException(ApiError error, Object... args) {
        super(String.format(error.getMessage(), args));
        this.apiError = error;
        this.formattedMessage = String.format(error.getMessage(), args);
    }

    public BaseApiException(ApiError apiError, String message) {
        super(message);
        this.apiError = apiError;
        this.formattedMessage = message;
    }
}

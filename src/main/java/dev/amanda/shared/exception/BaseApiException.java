package dev.amanda.shared.exception;

public abstract class BaseApiException extends RuntimeException {
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

    public ApiError getApiError() { return apiError; }
    public String getFormattedMessage() { return formattedMessage; }
}

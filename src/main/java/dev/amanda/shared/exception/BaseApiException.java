package dev.amanda.shared.exception;

public abstract class BaseApiException extends RuntimeException {
    private final ApiError apiError;

    public BaseApiException(ApiError apiError) {
        super(apiError.getMessage());
        this.apiError = apiError;
    }

    public BaseApiException(ApiError apiError, String message) {
        super(message);
        this.apiError = apiError;
    }

    public ApiError getApiError() { return apiError; }
}

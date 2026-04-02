package dev.amanda.shared.exception;

public class GenericApiException extends BaseApiException {
    public GenericApiException() {
        super(ApiError.INTERNAL_SERVER_ERROR);
    }
}

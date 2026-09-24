package dev.amanda.shared.exception;

public class NotAllowedException extends BaseApiException {
    public NotAllowedException() {
        super(ApiError.NOT_ALLOWED);
    }

    public NotAllowedException(String message) {
        super(ApiError.NOT_ALLOWED, message);
    }
}

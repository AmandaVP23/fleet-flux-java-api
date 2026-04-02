package dev.amanda.shared.exception;

public class ErrorResponse {
    public int errorCode;
    public String message;
    public int status;

    public ErrorResponse(ApiError error) {
        this.errorCode = error.getErrorCode();
        this.message = error.getMessage();
        this.status = error.getStatus();
    }

    public ErrorResponse(ApiError error, String message) {
        this.errorCode = error.getErrorCode();
        this.message = message;
        this.status = error.getStatus();
    }

    public ErrorResponse(int errorCode, String message, int status) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }
}

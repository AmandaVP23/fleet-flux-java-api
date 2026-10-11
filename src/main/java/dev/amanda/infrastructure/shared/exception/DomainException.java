package dev.amanda.infrastructure.shared.exception;

public class DomainException extends RuntimeException {
    private final DomainError error;

    public DomainException(DomainError error) {
        super(error.getMessage());
        this.error = error;
    }

    public DomainError getError() {
        return error;
    }
}

package dev.amanda.shared.exception;

public enum ApiError {
    INTERNAL_SERVER_ERROR(0, "Internal server error", 500),
    ORGANIZATION_WITH_SAME_NAME_ALREADY_EXISTS(100, "Organization with same name already exists", 409),
    ORGANIZATION_WITH_SAME_GENERATED_REALM_ALREADY_EXISTS(101, "Organization with same generated realm already exists", 409),
    REALM_KEYCLOAK_CONFLICT(102, "Keycloak conflict creating realm: %s", 409);

    private final int errorCode;
    private final String message;
    private final int status;

    ApiError(int errorCode, String message, int status) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }

    public int getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public int getStatus() { return status; }
}


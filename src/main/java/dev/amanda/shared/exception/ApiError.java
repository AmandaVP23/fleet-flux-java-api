package dev.amanda.shared.exception;

public enum ApiError {
    INTERNAL_SERVER_ERROR(0, "Internal server error", 500),
    VALIDATION_ERROR(1, "Validation error: %s", 400),
    GENERIC_BAD_REQUEST(2, "%s", 400),
    ORGANIZATION_WITH_SAME_NAME_ALREADY_EXISTS(100, "Organization with same name already exists", 409),
    ORGANIZATION_WITH_SAME_GENERATED_REALM_ALREADY_EXISTS(101, "Organization with same generated realm already exists", 409),
    REALM_KEYCLOAK_CONFLICT(102, "Keycloak conflict creating realm: %s", 409),
    USER_WITH_EMAIL_ALREADY_EXISTS(103, "User with same email already exists", 409),
    ORGANIZATION_NOT_FOUND(104, "Organization not found", 404),
    ORGANIZATION_INACTIVE(105, "Organization is deleted", 422),
    ORGANIZATION_NOT_INACTIVE(106, "Organization is not deleted", 422),
    USER_NOT_FOUND(107, "User not found", 404),
    NOT_ALLOWED(108, "Not allowed", 403),
    VEHICLE_BRAND_NOT_FOUND(107, "User not found", 404),
    VEHICLE_BRAND_DELETED(106, "Vehicle brand is already deleted", 422),;

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

    public String format(Object... args) {
        return String.format(message, args);
    }
}


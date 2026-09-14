package dev.amanda.shared.exception;

import lombok.Getter;

@Getter
public enum ApiError {
    INTERNAL_SERVER_ERROR(0, "Internal server error", 500),
    VALIDATION_ERROR(1, "Validation error: %s", 400),
    GENERIC_BAD_REQUEST(2, "%s", 400),
    NOT_ALLOWED(3, "Not allowed", 403),
    ORGANIZATION_WITH_SAME_NAME_ALREADY_EXISTS(100, "Organization with same name already exists", 409),
    ORGANIZATION_WITH_SAME_GENERATED_REALM_ALREADY_EXISTS(101, "Organization with same generated realm already exists", 409),
    ORGANIZATION_WITH_SAME_SLUG_ALREADY_EXISTS(102, "Organization with same slug already exists", 409),
    REALM_KEYCLOAK_CONFLICT(103, "Keycloak conflict creating realm: %s", 409),
    USER_WITH_EMAIL_ALREADY_EXISTS(104, "User with same email already exists", 409),
    ORGANIZATION_NOT_FOUND(105, "Organization not found", 404),
    ORGANIZATION_INACTIVE(106, "Organization is deleted", 422),
    ORGANIZATION_NOT_INACTIVE(107, "Organization is not deleted", 422),
    USER_NOT_FOUND(108, "User not found", 404),
    VEHICLE_BRAND_NOT_FOUND(109, "User not found", 404),
    VEHICLE_BRAND_DELETED(110, "Vehicle brand is already deleted", 422),
    VEHICLE_BRAND_IS_USED(111, "Vehicle brand is being used", 400),
    VEHICLE_NOT_FOUND(112, "Vehicle not found", 404),;

    private final int errorCode;
    private final String message;
    private final int status;

    ApiError(int errorCode, String message, int status) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}


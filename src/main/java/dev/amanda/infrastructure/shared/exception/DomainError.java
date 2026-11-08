package dev.amanda.infrastructure.shared.exception;

import lombok.Getter;

@Getter
public enum DomainError {
    VEHICLE_DRIVER_ASSIGNMENT_START_DATETIME_BEFORE_END_DATETIME(
            "Vehicle-driver assignment start datetime should be before end datetime"
    ),
    INVALID_COORDINATES("Latitude must be between -90 and 90 and longitude between -180 and 180"),;

    private final String message;

    DomainError(String message) {
        this.message = message;
    }

}

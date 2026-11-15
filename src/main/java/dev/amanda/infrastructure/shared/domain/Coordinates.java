package dev.amanda.infrastructure.shared.domain;

import dev.amanda.infrastructure.shared.exception.DomainError;
import dev.amanda.infrastructure.shared.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Coordinates(
        @Column(nullable = false) double latitude,
        @Column(nullable = false) double longitude
) {
    public Coordinates {
        if (Double.isNaN(latitude) || latitude < -90 || latitude > 90
                || Double.isNaN(longitude) || longitude < -180 || longitude > 180) {
            throw new DomainException(DomainError.INVALID_COORDINATES);
        }
    }
}
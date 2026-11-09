package dev.amanda.route.dto;

import dev.amanda.infrastructure.shared.domain.Coordinates;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateRouteRequestDTO(
        @NotNull Long vehicleId,
        @NotNull Instant scheduledTime,
        @NotNull Coordinates origin,
        @NotNull Coordinates destination
) {}

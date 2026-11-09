package dev.amanda.route.domain;

import dev.amanda.infrastructure.shared.domain.Coordinates;

import java.util.List;

public record RoutePlan(
        double distanceMeters,
        double durationSeconds,
        List<Coordinates> geometry
) {
    public RoutePlan {
        geometry = List.copyOf(geometry);
    }
}

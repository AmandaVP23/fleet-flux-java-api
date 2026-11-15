package dev.amanda.route.domain;

public record RoutePreview(
        String geometry,
        int geometryPrecision,
        double distanceMeters,
        double durationSeconds
) {
}

package dev.amanda.route.domain;

import java.util.List;

public record RouteDetails(
        String geometry,
        int geometryPrecision,
        double distanceMeters,
        double durationSeconds,
        List<Double> segmentDistancesInMeters,
        List<Double> segmentDurationsInSeconds
) {
    public RouteDetails {
        segmentDistancesInMeters = List.copyOf(segmentDistancesInMeters);
        segmentDurationsInSeconds = List.copyOf(segmentDurationsInSeconds);
    }
}

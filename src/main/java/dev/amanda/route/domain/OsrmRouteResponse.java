package dev.amanda.route.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OsrmRouteResponse(
        String code,
        List<Route> routes
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Route(
            double distance, // meters
            double duration, // seconds
            String geometry, // encoded polyline, precision depends on the requested geometries=polyline|polyline6
            List<Leg> legs
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Leg(
            Annotation annotation // only present when requested with annotations=...
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Annotation(
            List<Double> distance, // meters
            List<Double> duration  // seconds
    ) {
    }
}

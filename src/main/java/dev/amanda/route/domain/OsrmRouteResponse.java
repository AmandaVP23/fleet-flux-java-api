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
            Geometry geometry // only present with overview=simplified|full and geometries=geojson
    ) {
    }

    /**
     * Each position is [longitude, latitude].
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Geometry(
            String type,
            List<List<Double>> coordinates
    ) {
    }
}

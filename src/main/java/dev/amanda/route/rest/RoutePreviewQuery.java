package dev.amanda.route.rest;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;

@Getter
public class RoutePreviewQuery {
    @QueryParam("originLon")
    @NotNull()
    private double originLon;

    @QueryParam("originLat")
    @NotNull()
    private double originLat;

    @QueryParam("destinationLon")
    @NotNull()
    private double destinationLon;

    @QueryParam("destinationLat")
    @NotNull()
    private double destinationLat;
}

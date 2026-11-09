package dev.amanda.route.domain;

import dev.amanda.infrastructure.shared.domain.Coordinates;
import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@JBossLog
public class OsrmRoutingService {

    private static final String OSRM_OK = "Ok";

    private static final String OVERVIEW = "full";
    private static final String GEOMETRIES = "polyline6";
    public static final int GEOMETRY_PRECISION = 6;

    private static final String ANNOTATIONS = "distance,duration";

    @Inject
    @RestClient
    OsrmClient osrmClient;

    public RoutePreview preview(Coordinates origin, Coordinates destination) {
        OsrmRouteResponse.Route route = fetchBestRoute(origin, destination, null);

        return new RoutePreview(
                route.geometry(),
                GEOMETRY_PRECISION,
                route.distance(),
                route.duration()
        );
    }

    public RouteDetails details(Coordinates origin, Coordinates destination) {
        OsrmRouteResponse.Route route = fetchBestRoute(origin, destination, ANNOTATIONS);

        List<Double> segmentDistancesInMeters = new ArrayList<>();
        List<Double> segmentDurationsInSeconds = new ArrayList<>();

        // One leg per waypoint pair: flatten them in order into a single list per annotation
        if (route.legs() == null || route.legs().isEmpty()) {
            log.errorf("OSRM response without legs for %s -> %s", origin, destination);
            throw new BaseApiException(ApiError.ROUTING_SERVICE_UNAVAILABLE);
        }

        for (OsrmRouteResponse.Leg leg : route.legs()) {
            OsrmRouteResponse.Annotation annotation = leg.annotation();
            if (annotation == null || annotation.distance() == null || annotation.duration() == null) {
                log.errorf("OSRM response without annotations for %s -> %s", origin, destination);
                throw new BaseApiException(ApiError.ROUTING_SERVICE_UNAVAILABLE);
            }
            segmentDistancesInMeters.addAll(annotation.distance());
            segmentDurationsInSeconds.addAll(annotation.duration());
        }

        return new RouteDetails(
                route.geometry(),
                GEOMETRY_PRECISION,
                route.distance(),
                route.duration(),
                segmentDistancesInMeters,
                segmentDurationsInSeconds
        );
    }

    private OsrmRouteResponse.Route fetchBestRoute(Coordinates origin, Coordinates destination, String annotations) {
        String coordinatesStr = String.format("%f,%f;%f,%f", origin.longitude(), origin.latitude(), destination.longitude(), destination.latitude());

        OsrmRouteResponse response = fetchRoute(coordinatesStr, annotations);

        if (response == null
                || !OSRM_OK.equals(response.code())
                || response.routes() == null
                || response.routes().isEmpty()) {
            log.debugf("OSRM returned no route for %s: %s", coordinatesStr, response);
            throw new BaseApiException(ApiError.ROUTE_NOT_FOUND);
        }

        OsrmRouteResponse.Route route = response.routes().get(0);

        if (route.geometry() == null || route.geometry().isBlank()) {
            log.errorf("OSRM response without geometry for %s", coordinatesStr);
            throw new BaseApiException(ApiError.ROUTING_SERVICE_UNAVAILABLE);
        }

        return route;
    }

    private OsrmRouteResponse fetchRoute(String coordinates, String annotations) {
        try {
            return osrmClient.getRoute(coordinates, OVERVIEW, GEOMETRIES, annotations);
        } catch (WebApplicationException ex) {
            if (ex.getResponse().getStatus() == 400) {
                log.debugf(ex, "OSRM rejected the query %s", coordinates);
                throw new BaseApiException(ApiError.ROUTE_NOT_FOUND);
            }
            log.errorf(ex, "OSRM responded with an error for %s", coordinates);
            throw new BaseApiException(ApiError.ROUTING_SERVICE_UNAVAILABLE);
        } catch (ProcessingException ex) {
            log.errorf(ex, "Could not reach OSRM for %s", coordinates);
            throw new BaseApiException(ApiError.ROUTING_SERVICE_UNAVAILABLE);
        }
    }
}

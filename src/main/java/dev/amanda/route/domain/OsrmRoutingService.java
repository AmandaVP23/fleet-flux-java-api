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

import java.util.List;

@ApplicationScoped
@JBossLog
public class OsrmRoutingService {

    private static final String OSRM_OK = "Ok";

    @Inject
    @RestClient
    OsrmClient osrmClient;

    public RoutePlan plan(Coordinates origin, Coordinates destination) {
        String coordinatesStr = String.format("%f,%f;%f,%f", origin.longitude(), origin.latitude(), destination.longitude(), destination.latitude());

        OsrmRouteResponse response = fetchRoute(coordinatesStr);

        if (response == null
                || !OSRM_OK.equals(response.code())
                || response.routes() == null
                || response.routes().isEmpty()) {
            log.debugf("OSRM returned no route for %s: %s", coordinatesStr, response);
            throw new BaseApiException(ApiError.ROUTE_NOT_FOUND);
        }

        OsrmRouteResponse.Route route = response.routes().get(0);

        if (route.geometry() == null || route.geometry().coordinates() == null) {
            log.errorf("OSRM response without geometry for %s", coordinatesStr);
            throw new BaseApiException(ApiError.ROUTING_SERVICE_UNAVAILABLE);
        }

        return new RoutePlan(route.distance(), route.duration(), toCoordinates(route.geometry()));
    }

    // GeoJSON positions are [longitude, latitude]
    private List<Coordinates> toCoordinates(OsrmRouteResponse.Geometry geometry) {
        return geometry.coordinates().stream()
                .map(position -> new Coordinates(position.get(1), position.get(0)))
                .toList();
    }

    private OsrmRouteResponse fetchRoute(String coordinates) {
        try {
            return osrmClient.getRoutePlan(coordinates, "full", "geojson");
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

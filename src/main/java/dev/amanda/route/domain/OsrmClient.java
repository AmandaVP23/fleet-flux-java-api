package dev.amanda.route.domain;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey="osrm-api")
public interface OsrmClient {

    @GET
    @Path("/route/v1/driving/{coordinates}")
    OsrmRouteResponse getRoute(
            @PathParam("coordinates") String coordinates,
            @QueryParam("overview") String overview,
            @QueryParam("geometries") String geometries,
            @QueryParam("annotations") String annotations
    );
}

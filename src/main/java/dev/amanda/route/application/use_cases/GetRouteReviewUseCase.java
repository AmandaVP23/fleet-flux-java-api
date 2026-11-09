package dev.amanda.route.application.use_cases;

import dev.amanda.infrastructure.shared.domain.Coordinates;
import dev.amanda.route.domain.OsrmRoutingService;
import dev.amanda.route.domain.RoutePreview;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetRouteReviewUseCase {

    @Inject
    OsrmRoutingService osrmRoutingService;

    public RoutePreview execute(Coordinates origin, Coordinates destination) {
        return osrmRoutingService.preview(origin, destination);
    }
}

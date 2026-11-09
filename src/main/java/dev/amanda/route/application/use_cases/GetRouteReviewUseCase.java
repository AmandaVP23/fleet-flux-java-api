package dev.amanda.route.application.use_cases;

import dev.amanda.infrastructure.shared.domain.Coordinates;
import dev.amanda.route.domain.OsrmRoutingService;
import dev.amanda.route.domain.RoutePlan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetRouteReviewUseCase {

    @Inject
    OsrmRoutingService osrmRoutingService;

    public RoutePlan execute(Coordinates origin, Coordinates destination) {
        return osrmRoutingService.plan(origin, destination);
    }
}

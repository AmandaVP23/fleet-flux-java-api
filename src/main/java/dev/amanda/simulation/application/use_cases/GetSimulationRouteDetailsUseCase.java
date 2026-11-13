package dev.amanda.simulation.application.use_cases;

import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.route.domain.RouteDetailsEntity;
import dev.amanda.simulation.application.mappers.SimulationRouteDetailsMapper;
import dev.amanda.simulation.domain.SimulationFleetRepository;
import dev.amanda.simulation.dto.SimulationRouteDetailsResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GetSimulationRouteDetailsUseCase {

    @Inject
    SimulationFleetRepository simulationFleetRepository;

    @Inject
    SimulationRouteDetailsMapper simulationRouteDetailsMapper;

    @Transactional
    public SimulationRouteDetailsResponseDTO execute(long routeId) {
        RouteDetailsEntity routeDetailsEntity = simulationFleetRepository
                .getSimulationRouteDetails(routeId)
                .orElseThrow(() -> new BaseApiException(ApiError.ROUTE_DETAILS_NOT_FOUND));

        return simulationRouteDetailsMapper.toDto(routeDetailsEntity);
    }
}

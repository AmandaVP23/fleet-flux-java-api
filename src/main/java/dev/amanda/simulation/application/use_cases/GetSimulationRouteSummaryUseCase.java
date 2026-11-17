package dev.amanda.simulation.application.use_cases;

import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.route.application.mappers.RouteMapper;
import dev.amanda.route.dto.RouteResponseDTO;
import dev.amanda.simulation.domain.SimulationFleetRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GetSimulationRouteSummaryUseCase {

    private final SimulationFleetRepository simulationFleetRepository;
    private final RouteMapper routeMapper;

    @Transactional
    public RouteResponseDTO execute(long routeId) {
        return simulationFleetRepository.getSimulationRoute(routeId)
                .map(routeMapper::toDto)
                .orElseThrow(() -> new BaseApiException(ApiError.ROUTE_NOT_FOUND));
    }
}

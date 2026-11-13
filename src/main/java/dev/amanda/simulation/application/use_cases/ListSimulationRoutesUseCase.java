package dev.amanda.simulation.application.use_cases;

import dev.amanda.route.application.mappers.RouteMapper;
import dev.amanda.route.dto.RouteResponseDTO;
import dev.amanda.simulation.domain.SimulationFleetRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ListSimulationRoutesUseCase {

    @Inject
    SimulationFleetRepository simulationFleetRepository;

    @Inject
    RouteMapper routeMapper;

    // Transactional so the lazy Route.vehicle can be read while mapping
    @Transactional
    public List<RouteResponseDTO> execute() {
        return simulationFleetRepository.getSimulationRoutes().stream()
                .map(routeMapper::toDto)
                .toList();
    }
}

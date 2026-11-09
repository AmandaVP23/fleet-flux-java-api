package dev.amanda.route.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.route.application.mappers.RouteMapper;
import dev.amanda.route.domain.Route;
import dev.amanda.route.domain.RouteRepository;
import dev.amanda.route.domain.RouteStatus;
import dev.amanda.route.dto.CreateRouteRequestDTO;
import dev.amanda.route.dto.RouteResponseDTO;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;

@ApplicationScoped
public class CreateRouteUseCase {

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    RouteRepository routeRepository;

    @Inject
    RouteMapper routeMapper;

    @Transactional
    public RouteResponseDTO execute(CreateRouteRequestDTO dto, AuthContext authContext) {
        Vehicle vehicle = vehicleRepository.findByIdInOrganizationOrThrow(dto.vehicleId(), authContext.getOrganizationId());

        if (dto.scheduledTime().isBefore(Instant.now())) {
            throw new BaseApiException(ApiError.ROUTE_SCHEDULED_DATE_MUST_BE_FUTURE);
        }

        if (dto.origin().equals(dto.destination())) {
            throw new BaseApiException(ApiError.ORIGIN_MUST_BE_DIFFERENT_THAN_DESTINATION);
        }

        Route route = new Route();
        routeMapper.updateRouteFromDto(dto, route);
        route.setVehicle(vehicle);
        route.setStatus(RouteStatus.SCHEDULED);

        routeRepository.persist(route);

        return routeMapper.toDto(route);
    }
}

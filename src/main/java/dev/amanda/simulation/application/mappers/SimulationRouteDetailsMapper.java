package dev.amanda.simulation.application.mappers;

import dev.amanda.route.domain.RouteDetailsEntity;
import dev.amanda.simulation.dto.SimulationRouteDetailsResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface SimulationRouteDetailsMapper {

    @Mapping(target = "routeId", source = "route.id")
    SimulationRouteDetailsResponseDTO toDto(RouteDetailsEntity entity);
}

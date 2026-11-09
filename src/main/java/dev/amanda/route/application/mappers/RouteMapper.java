package dev.amanda.route.application.mappers;

import dev.amanda.infrastructure.shared.application.BaseMapper;
import dev.amanda.route.domain.Route;
import dev.amanda.route.dto.CreateRouteRequestDTO;
import dev.amanda.route.dto.RouteResponseDTO;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.dto.VehicleSummaryResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "jakarta", uses = BaseMapper.class)
public interface RouteMapper {

    RouteResponseDTO toDto(Route route);

    VehicleSummaryResponseDTO toDto(Vehicle vehicle);

    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateRouteFromDto(CreateRouteRequestDTO dto, @MappingTarget Route route);
}

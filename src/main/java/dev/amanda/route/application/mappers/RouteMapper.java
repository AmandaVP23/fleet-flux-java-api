package dev.amanda.route.application.mappers;

import dev.amanda.infrastructure.shared.application.BaseMapper;
import dev.amanda.route.domain.Route;
import dev.amanda.route.domain.RouteDetails;
import dev.amanda.route.domain.RouteDetailsEntity;
import dev.amanda.route.dto.CreateRouteRequestDTO;
import dev.amanda.route.dto.RouteResponseDTO;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.dto.VehicleSummaryResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "jakarta", uses = BaseMapper.class)
public interface RouteMapper {

    @Mapping(target = "distanceMeters", source = "details.distanceMeters")
    @Mapping(target = "durationSeconds", source = "details.durationSeconds")
    RouteResponseDTO toDto(Route route, RouteDetails details);

    VehicleSummaryResponseDTO toDto(Vehicle vehicle);

    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateRouteFromDto(CreateRouteRequestDTO dto, @MappingTarget Route route);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "route", source = "route")
    RouteDetailsEntity toEntity(RouteDetails details, Route route);
}

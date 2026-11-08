package dev.amanda.vehicle_driver_assignment.application.mappers;

import dev.amanda.infrastructure.shared.application.BaseMapper;
import dev.amanda.user.domain.User;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignment;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta", uses = { BaseMapper.class })
public interface VehicleDriverAssignmentMapper {
    VehicleDriverAssignmentResponseDTO toDto(VehicleDriverAssignment vehicleDriverAssignment);

    @Mapping(target = "brandName", source = "vehicle.brand.name")
    VehicleDriverAssignmentResponseDTO.VehicleDTO toVehicleDto(
            Vehicle vehicle
    );

    VehicleDriverAssignmentResponseDTO.DriverDTO toDriverDto(
            User driver
    );
}

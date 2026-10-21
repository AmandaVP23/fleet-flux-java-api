package dev.amanda.vehicle_device_assignment.application.mappers;

import dev.amanda.infrastructure.shared.application.BaseMapper;
import dev.amanda.vehicle_device_assignment.domain.VehicleDeviceAssignment;
import dev.amanda.vehicle_device_assignment.dto.VehicleDeviceAssignmentResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta", uses = BaseMapper.class)
public interface VehicleDeviceAssignmentMapper {
    VehicleDeviceAssignmentResponseDTO toDTO(VehicleDeviceAssignment vehicleDeviceAssignment);
}

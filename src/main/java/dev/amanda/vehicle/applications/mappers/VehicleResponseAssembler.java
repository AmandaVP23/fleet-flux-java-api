package dev.amanda.vehicle.applications.mappers;

import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.dto.VehicleDriverDTO;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import dev.amanda.vehicle_device_assignment.domain.VehicleDeviceAssignmentRepository;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class VehicleResponseAssembler {

    @Inject
    VehicleMapper vehicleMapper;

    @Inject
    VehicleDeviceAssignmentRepository vehicleDeviceAssignmentRepository;

    @Inject
    VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;

    public List<VehicleResponseDTO> toResponses(List<Vehicle> vehicles) {
        List<Long> ids = vehicles.stream().map(Vehicle::getId).toList();

        Map<Long, Long> devicesResult = vehicleDeviceAssignmentRepository.findActiveDeviceIdsByVehicleIds(ids);

        Map<Long, VehicleDriverDTO> driversResult = vehicleDriverAssignmentRepository.findActiveDriverIdsByVehicleIds(ids);

        List<VehicleResponseDTO> data = vehicles.stream().map(v -> {
            var dto = vehicleMapper.toDto(v);
            dto.setDeviceId(devicesResult.get(v.getId()));
            dto.setDriver(driversResult.get(v.getId()));
            return dto;
        }).toList();

        return data;
    }

    public VehicleResponseDTO toResponse(Vehicle vehicle) {
        return toResponses(List.of(vehicle)).getFirst();
    }
}

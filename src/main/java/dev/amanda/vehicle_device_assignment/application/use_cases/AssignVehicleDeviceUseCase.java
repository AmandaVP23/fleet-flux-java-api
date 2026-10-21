package dev.amanda.vehicle_device_assignment.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.tracking_device.domain.TrackingDevice;
import dev.amanda.tracking_device.persistence.TrackingDeviceRepositoryPanache;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.persistence.VehicleRepositoryPanache;
import dev.amanda.vehicle_device_assignment.application.mappers.VehicleDeviceAssignmentMapper;
import dev.amanda.vehicle_device_assignment.domain.VehicleDeviceAssignment;
import dev.amanda.vehicle_device_assignment.domain.VehicleDeviceAssignmentRepository;
import dev.amanda.vehicle_device_assignment.dto.VehicleDeviceAssignmentDTO;
import dev.amanda.vehicle_device_assignment.dto.VehicleDeviceAssignmentResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.java.Log;

import java.time.Instant;

@Log
@ApplicationScoped
public class AssignVehicleDeviceUseCase {

    @Inject
    TrackingDeviceRepositoryPanache trackingDeviceRepositoryPanache;

    @Inject
    VehicleRepositoryPanache vehicleRepositoryPanache;

    @Inject
    VehicleDeviceAssignmentRepository vehicleDeviceAssignmentRepository;

    @Inject
    VehicleDeviceAssignmentMapper vehicleDeviceAssignmentMapper;

    public VehicleDeviceAssignmentResponseDTO execute(VehicleDeviceAssignmentDTO dto, AuthContext authContext) {
        Vehicle vehicle = vehicleRepositoryPanache.findByIdInOrganizationOrThrow(dto.vehicleId(), authContext.getOrganizationId());

        TrackingDevice trackingDevice = trackingDeviceRepositoryPanache.findByIdInOrganizationOrThrow(dto.deviceId(), authContext.getOrganizationId());

        VehicleDeviceAssignment vehicleDeviceAssignment = new VehicleDeviceAssignment();
        vehicleDeviceAssignment.setVehicle(vehicle);
        vehicleDeviceAssignment.setDevice(trackingDevice);
        vehicleDeviceAssignment.setAssignedAt(Instant.now());

        vehicleDeviceAssignmentRepository.persist(vehicleDeviceAssignment);

        return vehicleDeviceAssignmentMapper.toDTO(vehicleDeviceAssignment);
    }
}

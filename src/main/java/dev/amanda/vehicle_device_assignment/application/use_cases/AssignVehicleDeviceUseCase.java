package dev.amanda.vehicle_device_assignment.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.tracking_device.domain.TrackingDevice;
import dev.amanda.tracking_device.domain.TrackingDeviceRepository;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle_device_assignment.application.mappers.VehicleDeviceAssignmentMapper;
import dev.amanda.vehicle_device_assignment.domain.VehicleDeviceAssignment;
import dev.amanda.vehicle_device_assignment.domain.VehicleDeviceAssignmentRepository;
import dev.amanda.vehicle_device_assignment.dto.VehicleDeviceAssignmentDTO;
import dev.amanda.vehicle_device_assignment.dto.VehicleDeviceAssignmentResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

@Log
@ApplicationScoped
public class AssignVehicleDeviceUseCase {

    @Inject
    TrackingDeviceRepository trackingDeviceRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    VehicleDeviceAssignmentRepository vehicleDeviceAssignmentRepository;

    @Inject
    VehicleDeviceAssignmentMapper vehicleDeviceAssignmentMapper;

    @Transactional
    public VehicleDeviceAssignmentResponseDTO execute(VehicleDeviceAssignmentDTO dto, AuthContext authContext) {
        Vehicle vehicle = vehicleRepository.findByIdInOrganizationOrThrow(dto.vehicleId(), authContext.getOrganizationId());

        TrackingDevice trackingDevice = trackingDeviceRepository.findByIdInOrganizationOrThrow(dto.deviceId(), authContext.getOrganizationId());

        vehicleDeviceAssignmentRepository.findActiveByVehicleAndDevice(dto.vehicleId(), dto.deviceId()).ifPresent(_ -> {
            throw new BaseApiException(ApiError.TRACKING_DEVICE_VEHICLE_ACTIVE_ASSIGNMENT_ALREADY_EXISTS);
        });

        vehicleDeviceAssignmentRepository.findActiveByVehicle(dto.vehicleId()).ifPresent(_ -> {
            throw new BaseApiException(ApiError.VEHICLE_ASSIGNED_TO_TRACKING_DEVICE_CONFLICT);
        });

        vehicleDeviceAssignmentRepository.findActiveByDevice(dto.deviceId()).ifPresent(_ -> {
            throw new BaseApiException(ApiError.TRACKING_DEVICE_ACTIVE_ASSIGNMENT_CONFLICT);
        });

        User assignedByUser = userRepository.findByKeycloakIdOrThrow(authContext.getUserKeycloakId());

        VehicleDeviceAssignment vehicleDeviceAssignment = new VehicleDeviceAssignment();
        vehicleDeviceAssignment.setVehicle(vehicle);
        vehicleDeviceAssignment.setDevice(trackingDevice);
        vehicleDeviceAssignment.setAssignedBy(assignedByUser.getId());

        vehicleDeviceAssignmentRepository.persist(vehicleDeviceAssignment);

        return vehicleDeviceAssignmentMapper.toDTO(vehicleDeviceAssignment);
    }
}

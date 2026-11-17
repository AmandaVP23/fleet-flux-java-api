package dev.amanda.vehicle_driver_assignment.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.simulation.domain.SimulationConfigChanged;
import dev.amanda.user.domain.Roles;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle_driver_assignment.application.AssignmentConflictChecker;
import dev.amanda.vehicle_driver_assignment.application.mappers.VehicleDriverAssignmentMapper;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignment;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignmentRepository;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentRequestDTO;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.RequiredArgsConstructor;

@Log
@ApplicationScoped
@RequiredArgsConstructor
public class CreateVehicleDriverAssignmentUseCase {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;
    private final VehicleDriverAssignmentMapper vehicleDriverAssignmentMapper;
    private final AssignmentConflictChecker assignmentConflictChecker;
    private final Event<SimulationConfigChanged> events;

    @Transactional
    public VehicleDriverAssignmentResponseDTO execute(VehicleDriverAssignmentRequestDTO requestDTO, AuthContext auth) {
        User authenticatedUser = userRepository.findByKeycloakIdOrThrow(auth.getUserKeycloakId());
        User driver = auth.isSuperAdmin()
                ? userRepository.findByIdOrThrow(requestDTO.driverId)
                : userRepository.findByIdInOrganizationOrThrow(requestDTO.driverId, auth.getOrganizationId());

        if (!driver.getRole().getValue().equals(Roles.DRIVER)) {
            throw new BaseApiException(ApiError.USER_IS_NOT_DRIVER);
        }

        Vehicle vehicle = vehicleRepository.findByIdInOrganizationOrThrow(requestDTO.vehicleId, auth.getOrganizationId());

        VehicleDriverAssignment vehicleDriverAssignment = new VehicleDriverAssignment();
        vehicleDriverAssignment.setDriver(driver);
        vehicleDriverAssignment.setVehicle(vehicle);
        vehicleDriverAssignment.setStartDateTime(requestDTO.startDateTime);
        vehicleDriverAssignment.setEndDateTime(requestDTO.endDateTime);
        vehicleDriverAssignment.setAssignedBy(authenticatedUser);

        assignmentConflictChecker.checkVehicleDriverAssignmentConflict(vehicleDriverAssignment);

        vehicleDriverAssignmentRepository.persist(vehicleDriverAssignment);
        events.fire(SimulationConfigChanged.updateDevices());
        return vehicleDriverAssignmentMapper.toDto(vehicleDriverAssignment);
    }
}

package dev.amanda.vehicle_driver_assignment.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.user.domain.Roles;
import dev.amanda.user.domain.User;
import dev.amanda.user.persistence.UserRepositoryPersistence;
import dev.amanda.user.exceptions.UserNotFoundException;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle_driver_assignment.application.AssignmentConflictChecker;
import dev.amanda.vehicle_driver_assignment.application.mappers.VehicleDriverAssignmentMapper;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignment;
import dev.amanda.vehicle_driver_assignment.persistence.VehicleDriverAssignmentRepositoryPanache;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentListResponseDTO;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

@Log
@ApplicationScoped
public class CreateVehicleDriverAssignmentUseCase {

    @Inject
    UserRepositoryPersistence userRepositoryPersistence;

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    VehicleDriverAssignmentRepositoryPanache vehicleDriverAssignmentRepositoryPanache;

    @Inject
    VehicleDriverAssignmentMapper vehicleDriverAssignmentMapper;

    @Inject
    AssignmentConflictChecker assignmentConflictChecker;

    @Transactional
    public VehicleDriverAssignmentListResponseDTO execute(VehicleDriverAssignmentRequestDTO requestDTO, AuthContext auth) {
        User authenticatedUser = userRepositoryPersistence.findByKeycloakIdOrThrow(auth.getUserKeycloakId());
        User driver = userRepositoryPersistence.findByIdOrThrow(requestDTO.driverId);

        if (!driver.getOrganization().getId().equals(authenticatedUser.getOrganization().getId())) {
            throw new UserNotFoundException();
        }

        if (!driver.getRole().getValue().equals(Roles.DRIVER)) {
            throw new BaseApiException(ApiError.USER_IS_NOT_DRIVER);
        }

        Vehicle vehicle = vehicleRepository.findByIdOrThrow(requestDTO.vehicleId);

        VehicleDriverAssignment vehicleDriverAssignment = new VehicleDriverAssignment();
        vehicleDriverAssignment.setDriver(driver);
        vehicleDriverAssignment.setVehicle(vehicle);
        vehicleDriverAssignment.setStartDateTime(requestDTO.startDateTime);
        vehicleDriverAssignment.setEndDateTime(requestDTO.endDateTime);
        vehicleDriverAssignment.setAssignedBy(authenticatedUser);

        assignmentConflictChecker.checkVehicleDriverAssignmentConflict(vehicleDriverAssignment);

        vehicleDriverAssignmentRepositoryPanache.persist(vehicleDriverAssignment);
        return vehicleDriverAssignmentMapper.toDto(vehicleDriverAssignment);
    }
}

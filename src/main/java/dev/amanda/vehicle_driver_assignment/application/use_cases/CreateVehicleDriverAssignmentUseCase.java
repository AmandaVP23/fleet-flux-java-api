package dev.amanda.vehicle_driver_assignment.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.user.domain.Roles;
import dev.amanda.user.domain.User;
import dev.amanda.user.persistence.UserRepositoryPersistence;
import dev.amanda.user.exceptions.UserNotFoundException;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle_driver_assignment.application.mappers.VehicleDriverAssignmentMapper;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignment;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignmentRepository;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentListResponseDTO;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentRequestDTO;
import dev.amanda.vehicle_driver_assignment.exceptions.UserIsNotDriverException;
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
    VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;

    @Inject
    VehicleDriverAssignmentMapper vehicleDriverAssignmentMapper;

    @Transactional
    public VehicleDriverAssignmentListResponseDTO execute(VehicleDriverAssignmentRequestDTO requestDTO, AuthContext auth) {
        User authenticatedUser = userRepositoryPersistence.findByKeycloakIdOrThrow(auth.getUserKeycloakId());
        User driver = userRepositoryPersistence.findByIdOrThrow(requestDTO.driverId);

        // todo validate end date / start date

        if (!driver.getOrganization().getId().equals(authenticatedUser.getOrganization().getId())) {
            throw new UserNotFoundException();
        }

        if (!driver.getRole().getValue().equals(Roles.DRIVER)) {
            throw new UserIsNotDriverException();
        }

        Vehicle vehicle = vehicleRepository.findByIdOrThrow(requestDTO.vehicleId);

        VehicleDriverAssignment vehicleDriverAssignment = new VehicleDriverAssignment();
        vehicleDriverAssignment.setDriver(driver);
        vehicleDriverAssignment.setVehicle(vehicle);
        vehicleDriverAssignment.setStartDateTime(requestDTO.startDateTime);
        vehicleDriverAssignment.setEndDateTime(requestDTO.endDateTime);
        vehicleDriverAssignment.setAssignedBy(authenticatedUser);

        vehicleDriverAssignmentRepository.persist(vehicleDriverAssignment);
        return vehicleDriverAssignmentMapper.toDto(vehicleDriverAssignment);
    }
}

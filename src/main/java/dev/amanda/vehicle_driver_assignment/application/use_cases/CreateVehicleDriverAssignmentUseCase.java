package dev.amanda.vehicle_driver_assignment.application.use_cases;

import dev.amanda.user.domain.Role;
import dev.amanda.user.domain.Roles;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentRequestDTO;
import dev.amanda.vehicle_driver_assignment.exceptions.UserIsNotDriverException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.java.Log;

@Log
@ApplicationScoped
public class CreateVehicleDriverAssignmentUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    VehicleRepository vehicleRepository;

    public void execute(VehicleDriverAssignmentRequestDTO requestDTO) {

        User driver = userRepository.findByIdOrThrow(requestDTO.driverId);

        if (!driver.getRole().getValue().equals(Roles.DRIVER)) {
            throw new UserIsNotDriverException();
        }

        Vehicle vehicle = vehicleRepository.findByIdOrThrow(requestDTO.vehicleId);



    }
}

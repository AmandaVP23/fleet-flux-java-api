package dev.amanda.vehicle_driver_assignment.application;

import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignment;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AssignmentConflictChecker {
    @Inject
    VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;

    public void checkVehicleDriverAssignmentConflict(VehicleDriverAssignment vehicleDriverAssignment) {
        boolean hasVehicleConflict = vehicleDriverAssignmentRepository.verifyVehicleAssignmentHasConflict(
                vehicleDriverAssignment.getVehicle().getId(),
                vehicleDriverAssignment.getStartDateTime(),
                vehicleDriverAssignment.getEndDateTime()
        );

        if (hasVehicleConflict) {
            throw new BaseApiException(ApiError.VEHICLE_DRIVER_ASSIGNMENT_VEHICLE_CONFLICT);
        }

        boolean hasDriverConflict = vehicleDriverAssignmentRepository.verifyDriverAssignmentHasConflict(
                vehicleDriverAssignment.getDriver().getId(),
                vehicleDriverAssignment.getStartDateTime(),
                vehicleDriverAssignment.getEndDateTime()
        );

        if (hasDriverConflict) {
            throw new BaseApiException(ApiError.VEHICLE_DRIVER_ASSIGNMENT_DRIVER_CONFLICT);
        }
    }
}

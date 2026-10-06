package dev.amanda.vehicle_driver_assignment.application;

import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentRequestDTO;
import dev.amanda.vehicle_driver_assignment.persistence.VehicleDriverAssignmentRepositoryPanache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

// todo - maybe this is not needed
@ApplicationScoped
public class AssignmentConflictChecker {
    @Inject
    VehicleDriverAssignmentRepositoryPanache vehicleDriverAssignmentRepositoryPanache;

    public boolean checkVehicleDriverAssignmentConflict(VehicleDriverAssignmentRequestDTO requestDTO) {
        vehicleDriverAssignmentRepositoryPanache.checkVehicleAssignmentConflict(requestDTO.vehicleId, requestDTO.startDateTime, requestDTO.endDateTime);

        return false;
    }
}

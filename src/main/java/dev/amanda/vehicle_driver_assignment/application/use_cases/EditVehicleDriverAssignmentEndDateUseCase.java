package dev.amanda.vehicle_driver_assignment.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.vehicle_driver_assignment.application.AssignmentConflictChecker;
import dev.amanda.vehicle_driver_assignment.application.mappers.VehicleDriverAssignmentMapper;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignment;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignmentRepository;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentEndDateEditDTO;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

@Log
@ApplicationScoped
public class EditVehicleDriverAssignmentEndDateUseCase {

    @Inject
    VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;

    @Inject
    VehicleDriverAssignmentMapper vehicleDriverAssignmentMapper;

    @Inject
    AssignmentConflictChecker assignmentConflictChecker;

    @Transactional
    public VehicleDriverAssignmentResponseDTO execute(long id, VehicleDriverAssignmentEndDateEditDTO requestDTO, AuthContext auth) {
        VehicleDriverAssignment vehicleDriverAssignment = vehicleDriverAssignmentRepository.findByIdInOrganizationOrThrow(id, auth.getOrganizationId());

        vehicleDriverAssignment.setEndDateTime(requestDTO.endDateTime);

        assignmentConflictChecker.checkVehicleDriverAssignmentConflict(vehicleDriverAssignment);

        vehicleDriverAssignmentRepository.persist(vehicleDriverAssignment);
        return vehicleDriverAssignmentMapper.toDto(vehicleDriverAssignment);
    }
}

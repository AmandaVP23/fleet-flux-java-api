package dev.amanda.vehicle_driver_assignment.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.simulation.domain.SimulationConfigChanged;
import dev.amanda.vehicle_driver_assignment.application.AssignmentConflictChecker;
import dev.amanda.vehicle_driver_assignment.application.mappers.VehicleDriverAssignmentMapper;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignment;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignmentRepository;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentEndDateEditDTO;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.RequiredArgsConstructor;

@Log
@ApplicationScoped
@RequiredArgsConstructor
public class EditVehicleDriverAssignmentEndDateUseCase {

    private final VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;
    private final VehicleDriverAssignmentMapper vehicleDriverAssignmentMapper;
    private final AssignmentConflictChecker assignmentConflictChecker;
    private final Event<SimulationConfigChanged> events;

    @Transactional
    public VehicleDriverAssignmentResponseDTO execute(long id, VehicleDriverAssignmentEndDateEditDTO requestDTO, AuthContext auth) {
        VehicleDriverAssignment vehicleDriverAssignment = vehicleDriverAssignmentRepository.findByIdInOrganizationOrThrow(id, auth.getOrganizationId());

        vehicleDriverAssignment.setEndDateTime(requestDTO.endDateTime);

        assignmentConflictChecker.checkVehicleDriverAssignmentConflict(vehicleDriverAssignment);

        vehicleDriverAssignmentRepository.persist(vehicleDriverAssignment);
        events.fire(new SimulationConfigChanged(SimulationConfigChanged.Type.UPDATE_DEVICES, vehicleDriverAssignment.getVehicle().getId()));
        return vehicleDriverAssignmentMapper.toDto(vehicleDriverAssignment);
    }
}

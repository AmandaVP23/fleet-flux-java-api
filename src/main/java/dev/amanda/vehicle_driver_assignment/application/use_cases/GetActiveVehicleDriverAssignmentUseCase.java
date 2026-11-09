package dev.amanda.vehicle_driver_assignment.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.shared.application.OrganizationAccessService;
import dev.amanda.infrastructure.shared.exception.DomainError;
import dev.amanda.infrastructure.shared.exception.DomainException;
import dev.amanda.vehicle_driver_assignment.application.mappers.VehicleDriverAssignmentMapper;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignment;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignmentRepository;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;

@ApplicationScoped
public class GetActiveVehicleDriverAssignmentUseCase {
    @Inject
    VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;

    @Inject
    VehicleDriverAssignmentMapper vehicleDriverAssignmentMapper;


    public VehicleDriverAssignmentResponseDTO execute(AuthContext authContext, Long vehicleId, Long driverId, Instant at) {
        if (vehicleId == null && driverId == null) {
            throw new DomainException(DomainError.ACTIVE_ASSIGNMENT_REQUIRES_VEHICLE_OR_DRIVER);
        }

        Instant effectiveAt = at != null ? at : Instant.now();

        VehicleDriverAssignment vehicleDriverAssignment = vehicleDriverAssignmentRepository.findActiveAtByOrganization(effectiveAt, vehicleId, driverId, authContext.getOrganizationId());

        return vehicleDriverAssignmentMapper.toDto(vehicleDriverAssignment);
    }
}

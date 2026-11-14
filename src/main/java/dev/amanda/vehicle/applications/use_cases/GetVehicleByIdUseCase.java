package dev.amanda.vehicle.applications.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.vehicle.applications.mappers.VehicleResponseAssembler;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetVehicleByIdUseCase {

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    VehicleResponseAssembler vehicleResponseAssembler;

    public VehicleResponseDTO execute(long id, AuthContext authContext) {
        Vehicle vehicle = authContext.isSuperAdmin()
                ? vehicleRepository.findByIdOrThrow(id)
                : vehicleRepository.findByIdInOrganizationOrThrow(id, authContext.getOrganizationId());

        return vehicleResponseAssembler.toResponse(vehicle);
    }
}

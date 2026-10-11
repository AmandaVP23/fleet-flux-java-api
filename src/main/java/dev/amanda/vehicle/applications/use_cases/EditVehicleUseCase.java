package dev.amanda.vehicle.applications.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.vehicle.applications.mappers.VehicleMapper;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.dto.CreateVehicleRequestDTO;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import dev.amanda.vehicle.exceptions.VehicleNotFoundException;
import dev.amanda.vehicle.persistence.VehicleRepositoryPanache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Objects;

@ApplicationScoped
public class EditVehicleUseCase {

    @Inject
    VehicleRepositoryPanache vehicleRepositoryPanache;

    @Inject
    VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponseDTO execute(Long id, CreateVehicleRequestDTO createVehicleRequestDTO, AuthContext authContext) {
        Vehicle vehicle = vehicleRepositoryPanache.findByIdOrThrow(id);

        if (!authContext.isSuperAdmin() && !Objects.equals(authContext.getOrganizationId(), vehicle.getOrganization().getId())) {
            throw new VehicleNotFoundException();
        }

        vehicleMapper.updateVehicleFromDto(createVehicleRequestDTO, vehicle);

        vehicleRepositoryPanache.persist(vehicle);

        return vehicleMapper.toDto(vehicle);
    }
}

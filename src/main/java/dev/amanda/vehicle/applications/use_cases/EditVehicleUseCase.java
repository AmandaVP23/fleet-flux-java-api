package dev.amanda.vehicle.applications.use_cases;

import dev.amanda.oidc.AuthContext;
import dev.amanda.vehicle.applications.mappers.VehicleMapper;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle.dto.CreateVehicleRequestDTO;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import dev.amanda.vehicle.exceptions.VehicleNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Objects;

@ApplicationScoped
public class EditVehicleUseCase {

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponseDTO execute(Long id, CreateVehicleRequestDTO createVehicleRequestDTO, AuthContext authContext) {
        Vehicle vehicle = vehicleRepository.findByIdOrThrow(id);

        if (!authContext.isSuperAdmin() && !Objects.equals(authContext.getOrganizationId(), vehicle.getOrganization().getId())) {
            throw new VehicleNotFoundException();
        }

        vehicleMapper.updateVehicleFromDto(createVehicleRequestDTO, vehicle);

        vehicleRepository.persist(vehicle);

        return vehicleMapper.toDto(vehicle);
    }
}

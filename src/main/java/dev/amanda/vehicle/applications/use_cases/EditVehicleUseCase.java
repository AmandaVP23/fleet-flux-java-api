package dev.amanda.vehicle.applications.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.vehicle.applications.mappers.VehicleMapper;
import dev.amanda.vehicle.applications.mappers.VehicleResponseAssembler;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.dto.CreateVehicleRequestDTO;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import dev.amanda.vehicle.domain.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class EditVehicleUseCase {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final VehicleResponseAssembler vehicleResponseAssembler;

    @Transactional
    public VehicleResponseDTO execute(Long id, CreateVehicleRequestDTO createVehicleRequestDTO, AuthContext authContext) {
        Vehicle vehicle = authContext.isSuperAdmin()
                ? vehicleRepository.findByIdOrThrow(id)
                : vehicleRepository.findByIdInOrganizationOrThrow(id, authContext.getOrganizationId());

        vehicleMapper.updateVehicleFromDto(createVehicleRequestDTO, vehicle);

        vehicleRepository.persist(vehicle);

        return vehicleResponseAssembler.toResponse(vehicle);
    }
}

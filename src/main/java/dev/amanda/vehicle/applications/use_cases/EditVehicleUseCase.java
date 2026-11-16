package dev.amanda.vehicle.applications.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.simulation.domain.SimulationConfigChanged;
import dev.amanda.vehicle.applications.mappers.VehicleMapper;
import dev.amanda.vehicle.applications.mappers.VehicleResponseAssembler;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleStatus;
import dev.amanda.vehicle.domain.VehicleType;
import dev.amanda.vehicle.dto.CreateVehicleRequestDTO;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import dev.amanda.vehicle.domain.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@ApplicationScoped
@RequiredArgsConstructor
public class EditVehicleUseCase {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final VehicleResponseAssembler vehicleResponseAssembler;
    private final Event<SimulationConfigChanged> events;

    @Transactional
    public VehicleResponseDTO execute(Long id, CreateVehicleRequestDTO createVehicleRequestDTO, AuthContext authContext) {
        Vehicle vehicle = authContext.isSuperAdmin()
                ? vehicleRepository.findByIdOrThrow(id)
                : vehicleRepository.findByIdInOrganizationOrThrow(id, authContext.getOrganizationId());

        VehicleStatus previousStatus = vehicle.getStatus();
        VehicleType previousType = vehicle.getType();

        vehicleMapper.updateVehicleFromDto(createVehicleRequestDTO, vehicle);

        vehicleRepository.persist(vehicle);

        if (!Objects.equals(previousStatus, vehicle.getStatus()) || !Objects.equals(previousType, vehicle.getType())) {
            events.fire(new SimulationConfigChanged(SimulationConfigChanged.Type.UPDATE_DEVICES, vehicle.getId()));
        }

        return vehicleResponseAssembler.toResponse(vehicle);
    }
}

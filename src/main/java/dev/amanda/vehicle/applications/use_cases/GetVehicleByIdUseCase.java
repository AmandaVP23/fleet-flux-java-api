package dev.amanda.vehicle.applications.use_cases;

import dev.amanda.oidc.AuthContext;
import dev.amanda.vehicle.applications.mappers.VehicleMapper;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import dev.amanda.vehicle.exceptions.VehicleNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;

@ApplicationScoped
public class GetVehicleByIdUseCase {

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    VehicleMapper vehicleMapper;

    public VehicleResponseDTO execute(long id, AuthContext authContext) {
        Vehicle vehicle = vehicleRepository.findByIdOrThrow(id);

        if (!authContext.isSuperAdmin() && !Objects.equals(authContext.getOrganizationId(), vehicle.getOrganization().getId())) {
            throw new VehicleNotFoundException();
        }

        System.out.println(vehicle.getPlateNumber());

        return vehicleMapper.toDto(vehicle);
    }
}

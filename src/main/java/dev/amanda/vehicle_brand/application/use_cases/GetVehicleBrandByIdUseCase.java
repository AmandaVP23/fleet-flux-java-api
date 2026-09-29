package dev.amanda.vehicle_brand.application.use_cases;

import dev.amanda.vehicle_brand.application.mappers.VehicleBrandMapper;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.persistence.VehicleBrandRepositoryPanache;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import dev.amanda.vehicle_brand.exceptions.VehicleBrandNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetVehicleBrandByIdUseCase {

    @Inject
    VehicleBrandRepositoryPanache vehicleBrandRepositoryPanache;

    @Inject
    VehicleBrandMapper vehicleBrandMapper;

    public VehicleBrandResponseDTO execute(long id) {
        VehicleBrand vehicleBrand = vehicleBrandRepositoryPanache.findByIdOrThrow(id);

        if (vehicleBrand.getDeletedAt() != null) {
            throw new VehicleBrandNotFoundException();
        }

        return vehicleBrandMapper.toDto(vehicleBrand);
    }
}

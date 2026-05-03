package dev.amanda.vehicle_brand.application.use_cases;

import dev.amanda.vehicle_brand.application.mappers.VehicleBrandMapper;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.domain.VehicleBrandRepository;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetVehicleBrandByIdUseCase {

    @Inject
    VehicleBrandRepository vehicleBrandRepository;

    @Inject
    VehicleBrandMapper vehicleBrandMapper;

    public VehicleBrandResponseDTO execute(long id) {
        VehicleBrand vehicleBrand = vehicleBrandRepository.findByIdOrThrow(id);

        return vehicleBrandMapper.toDto(vehicleBrand);
    }
}

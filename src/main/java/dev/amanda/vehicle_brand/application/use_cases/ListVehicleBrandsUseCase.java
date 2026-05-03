package dev.amanda.vehicle_brand.application.use_cases;

import dev.amanda.vehicle_brand.application.mappers.VehicleBrandMapper;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.domain.VehicleBrandRepository;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListVehicleBrandsUseCase {

    @Inject
    VehicleBrandMapper vehicleBrandMapper;

    @Inject
    VehicleBrandRepository vehicleBrandRepository;

    public List<VehicleBrandResponseDTO> execute() {
        List<VehicleBrand> data = vehicleBrandRepository.listAll();

        return data
                .stream()
                .map(vehicleBrandMapper::toDto)
                .toList();
    }
}

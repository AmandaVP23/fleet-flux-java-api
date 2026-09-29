package dev.amanda.vehicle_brand.application.use_cases;

import dev.amanda.vehicle_brand.application.mappers.VehicleBrandMapper;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.persistence.VehicleBrandRepositoryPanache;
import dev.amanda.vehicle_brand.dto.CreateVehicleBrandRequestDTO;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateVehicleBrandUseCase {

    @Inject
    VehicleBrandRepositoryPanache vehicleBrandRepositoryPanache;

    @Inject
    VehicleBrandMapper vehicleBrandMapper;

    @Transactional
    public VehicleBrandResponseDTO execute(CreateVehicleBrandRequestDTO createVehicleBrandRequestDTO) {
        VehicleBrand vehicleBrand = new VehicleBrand();
        vehicleBrand.setName(createVehicleBrandRequestDTO.name);

        vehicleBrandRepositoryPanache.persist(vehicleBrand);

        return vehicleBrandMapper.toDto(vehicleBrand);
    }
}

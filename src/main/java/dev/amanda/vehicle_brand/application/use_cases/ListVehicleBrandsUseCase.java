package dev.amanda.vehicle_brand.application.use_cases;

import dev.amanda.oidc.AuthContext;
import dev.amanda.vehicle_brand.application.mappers.VehicleBrandMapper;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.domain.VehicleBrandRepository;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import dev.amanda.vehicle_brand.rest.VehicleBrandDeletionFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListVehicleBrandsUseCase {

    @Inject
    VehicleBrandMapper vehicleBrandMapper;

    @Inject
    VehicleBrandRepository vehicleBrandRepository;

    public List<VehicleBrandResponseDTO> execute(VehicleBrandDeletionFilter filter, AuthContext authContext) {
        VehicleBrandDeletionFilter effectiveFilter = filter;

        if (!authContext.isSuperAdmin()) {
            effectiveFilter = VehicleBrandDeletionFilter.ONLY_ACTIVE;
        }

        List<VehicleBrand> data = switch (effectiveFilter) {
            case VehicleBrandDeletionFilter.ALL -> vehicleBrandRepository.listAll();
            case ONLY_DELETED -> vehicleBrandRepository.listAllDeleted();
            case ONLY_ACTIVE -> vehicleBrandRepository.listAllActive();
        };

        return data
                .stream()
                .map(vehicleBrandMapper::toDto)
                .toList();
    }
}

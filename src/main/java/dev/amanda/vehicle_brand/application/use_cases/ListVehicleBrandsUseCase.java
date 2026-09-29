package dev.amanda.vehicle_brand.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.vehicle_brand.application.mappers.VehicleBrandMapper;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.persistence.VehicleBrandRepositoryPanache;
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
    VehicleBrandRepositoryPanache vehicleBrandRepositoryPanache;

    public List<VehicleBrandResponseDTO> execute(VehicleBrandDeletionFilter filter, AuthContext authContext) {
        VehicleBrandDeletionFilter effectiveFilter = filter;

        if (!authContext.isSuperAdmin()) {
            effectiveFilter = VehicleBrandDeletionFilter.ONLY_ACTIVE;
        }

        List<VehicleBrand> data = switch (effectiveFilter) {
            case VehicleBrandDeletionFilter.ALL -> vehicleBrandRepositoryPanache.listAll();
            case ONLY_DELETED -> vehicleBrandRepositoryPanache.listAllDeleted();
            case ONLY_ACTIVE -> vehicleBrandRepositoryPanache.listAllActive();
        };

        return data
                .stream()
                .map(vehicleBrandMapper::toDto)
                .toList();
    }
}

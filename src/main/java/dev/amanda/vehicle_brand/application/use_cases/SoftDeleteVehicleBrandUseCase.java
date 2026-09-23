package dev.amanda.vehicle_brand.application.use_cases;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle.rest.VehicleFilter;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.domain.VehicleBrandRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

import java.time.Instant;
import java.util.List;

@Log
@ApplicationScoped
public class SoftDeleteVehicleBrandUseCase {

    @Inject
    VehicleBrandRepository vehicleBrandRepository;

    @Inject
    VehicleRepository vehicleRepository;

    @Transactional
    public void execute(long id) {
        VehicleBrand vehicleBrand = vehicleBrandRepository.findByIdOrThrow(id);

        VehicleFilter vehicleFilter = new VehicleFilter();
        vehicleFilter.setBrandId(vehicleBrand.getId());

        List<Vehicle> vehicleUsingBrand = vehicleRepository.listPaginated(vehicleFilter, 0, 1, Sort.by("name").ascending());
        if (!vehicleUsingBrand.isEmpty()) {
            throw new BaseApiException(ApiError.VEHICLE_BRAND_IS_USED);
        }

        if (vehicleBrand.getDeletedAt() != null) {
            throw new BaseApiException(ApiError.VEHICLE_BRAND_DELETED, "Organization was already deleted");
        }

        vehicleBrand.setDeletedAt(Instant.now());
        vehicleBrandRepository.persist(vehicleBrand);

        log.info("Deleted vehicle brand with id " + id);
    }
}

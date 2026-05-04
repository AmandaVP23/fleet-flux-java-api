package dev.amanda.vehicle_brand.application.use_cases;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.domain.VehicleBrandRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

import java.time.Instant;

@Log
@ApplicationScoped
public class SoftDeleteVehicleBrandUseCase {

    @Inject
    VehicleBrandRepository vehicleBrandRepository;

    @Transactional
    public void execute(long id) {
        VehicleBrand vehicleBrand = vehicleBrandRepository.findByIdOrThrow(id);

        if (vehicleBrand.getDeletedAt() != null) {
            throw new BaseApiException(ApiError.VEHICLE_BRAND_DELETED, "Organization was already deleted");
        }

        vehicleBrand.setDeletedAt(Instant.now());
        vehicleBrandRepository.persist(vehicleBrand);

        log.info("Deleted vehicle brand with id " + id);
    }
}

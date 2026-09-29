package dev.amanda.vehicle_brand.domain;

import java.util.List;

public interface VehicleBrandRepository {
    VehicleBrand findByIdOrThrow(Long id);

    VehicleBrand findActiveByIdOrThrow(Long id);

    List<VehicleBrand> listAllDeleted();

    List<VehicleBrand> listAllActive();
}

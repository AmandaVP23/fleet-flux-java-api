package dev.amanda.vehicle.dto;

import dev.amanda.organization.domain.Organization;
import dev.amanda.vehicle_brand.domain.VehicleBrand;

public record VehicleResponseDTO(
    VehicleBrand vehicleBrand,
    String model,
    String variant,
    Organization organization
) {}

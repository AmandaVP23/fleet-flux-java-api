package dev.amanda.vehicle.dto;

import dev.amanda.infrastructure.shared.rest.current_year_max.CurrentYearMax;
import dev.amanda.infrastructure.shared.rest.organization_id_with_auth.HasOrganizationId;
import dev.amanda.infrastructure.shared.rest.organization_id_with_auth.ValidOrganizationIdWithAuth;
import dev.amanda.vehicle.domain.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@ValidOrganizationIdWithAuth()
public record CreateVehicleRequestDTO(
        @NotNull() Long brandId,
        String variant,
        Long organizationId,
        String model,
        @NotNull() String plateNumber,
        // todo validate vin
        String vin,
        @Min(1950)
        @CurrentYearMax()
        int yearOfManufacture,
        VehicleCategory category,
        VehicleType type,
        VehicleFuelType fuelType,
        double fuelCapacityInLiters,
        double avgConsumptionPer100Km,
        Instant lastServiceDate,
        String insurancePolicyNumber,
        Instant insuranceExpiryDate,
        Instant inspectionDueDate,
        VehicleOwnershipType ownershipType,
        @NotNull() VehicleStatus status
) implements HasOrganizationId {}

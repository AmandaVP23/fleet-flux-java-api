package dev.amanda.vehicle.dto;

import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.shared.application.BaseResponseDTO;
import dev.amanda.vehicle.domain.*;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class VehicleResponseDTO extends BaseResponseDTO {
    private String model;
    private String variant;
    private VehicleType type;
    private VehicleFuelType fuelType;
    private VehicleBrandResponseDTO brand;
    private OrganizationResponseDTO organization;
    private String plateNumber;
    private int yearOfManufacture;
    private VehicleStatus status;
    private String vin;
    private VehicleCategory category;
    private double fuelCapacityInLiters;
    private double avgConsumptionPer100Km;
    private Instant lastServiceDate;
    private String insurancePolicyNumber;
    private Instant insuranceExpiryDate;
    private Instant inspectionDueDate;
    private VehicleOwnershipType ownershipType;
}

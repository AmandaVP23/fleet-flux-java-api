package dev.amanda.vehicle.dto;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.shared.application.BaseResponseDTO;
import dev.amanda.vehicle.domain.VehicleFuelType;
import dev.amanda.vehicle.domain.VehicleType;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}

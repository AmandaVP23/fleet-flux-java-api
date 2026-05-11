package dev.amanda.vehicle.dto;

import dev.amanda.organization.domain.Organization;
import dev.amanda.shared.application.BaseResponseDTO;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleResponseDTO extends BaseResponseDTO {
    private VehicleBrand vehicleBrand;
    private String model;
    private String variant;
    private Organization organization;
}

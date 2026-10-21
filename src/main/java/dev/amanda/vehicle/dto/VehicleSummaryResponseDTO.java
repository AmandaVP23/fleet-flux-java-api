package dev.amanda.vehicle.dto;

import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.infrastructure.shared.application.BaseResponseDTO;
import dev.amanda.vehicle.domain.*;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class VehicleSummaryResponseDTO extends BaseResponseDTO {
    private String model;
    private String plateNumber;
    private VehicleCategory category;
}

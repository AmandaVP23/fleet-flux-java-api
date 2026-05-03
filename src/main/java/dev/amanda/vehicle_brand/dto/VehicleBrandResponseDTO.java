package dev.amanda.vehicle_brand.dto;

import dev.amanda.shared.application.BaseResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleBrandResponseDTO extends BaseResponseDTO {
    private String name;
}

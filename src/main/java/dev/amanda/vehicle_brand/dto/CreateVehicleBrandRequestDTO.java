package dev.amanda.vehicle_brand.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class CreateVehicleBrandRequestDTO {
    @NotBlank()
    @Length(min = 3, max = 80)
    public String name;
}

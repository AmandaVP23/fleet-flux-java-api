package dev.amanda.tracking_device.dto;

import dev.amanda.tracking_device.domain.TrackingDeviceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class CreateTrackingDeviceRequestDTO {
    @NotBlank()
    @Length(min = 15, max = 15)
    public String imei;

    @NotBlank()
    public String model;

    @NotNull
    public TrackingDeviceStatus status;
}

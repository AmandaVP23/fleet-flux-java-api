package dev.amanda.tracking_device.dto;

import dev.amanda.tracking_device.domain.TrackingDeviceStatus;
import jakarta.validation.constraints.NotNull;

public class ChangeTrackingDeviceStatusRequestDTO {
    @NotNull
    public TrackingDeviceStatus status;
}

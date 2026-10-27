package dev.amanda.tracking_device.dto;

import dev.amanda.infrastructure.shared.application.BaseResponseDTO;
import dev.amanda.tracking_device.domain.TrackingDeviceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackingDeviceResponseDTO extends BaseResponseDTO {
    private String imei;
    private String model;
    private TrackingDeviceStatus status;
}

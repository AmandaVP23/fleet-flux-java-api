package dev.amanda.vehicle_device_assignment.dto;

import dev.amanda.infrastructure.shared.application.BaseResponseDTO;
import dev.amanda.tracking_device.dto.TrackingDeviceResponseDTO;
import dev.amanda.vehicle.dto.VehicleSummaryResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class VehicleDeviceAssignmentResponseDTO extends BaseResponseDTO {
    private VehicleSummaryResponseDTO vehicle;
    private TrackingDeviceResponseDTO device;
    private Instant assignedAt;
}

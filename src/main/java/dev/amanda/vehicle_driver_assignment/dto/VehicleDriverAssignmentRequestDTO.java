package dev.amanda.vehicle_driver_assignment.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public class VehicleDriverAssignmentRequestDTO {
    @NotBlank()
    public long vehicleId;

    @NotBlank()
    public long driverId;

    @NotBlank()
    public Instant startDate;

    public Instant endDate;
}

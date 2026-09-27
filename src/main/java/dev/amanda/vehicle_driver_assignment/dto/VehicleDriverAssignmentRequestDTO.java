package dev.amanda.vehicle_driver_assignment.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class VehicleDriverAssignmentRequestDTO {
    @NotNull()
    public long vehicleId;

    @NotNull()
    public long driverId;

    @NotNull()
    public Instant startDateTime;

    public Instant endDateTime;
}

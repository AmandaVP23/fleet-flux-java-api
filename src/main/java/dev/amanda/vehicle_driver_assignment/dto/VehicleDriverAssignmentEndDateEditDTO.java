package dev.amanda.vehicle_driver_assignment.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class VehicleDriverAssignmentEndDateEditDTO {

    @NotNull()
    public Instant endDateTime;
}

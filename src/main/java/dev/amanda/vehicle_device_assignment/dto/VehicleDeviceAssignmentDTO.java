package dev.amanda.vehicle_device_assignment.dto;

import jakarta.validation.constraints.NotNull;

public record VehicleDeviceAssignmentDTO(
        @NotNull() Long vehicleId,
        @NotNull() Long deviceId
) {}

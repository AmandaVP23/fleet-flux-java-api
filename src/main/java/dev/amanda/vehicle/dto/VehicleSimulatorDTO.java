package dev.amanda.vehicle.dto;

import dev.amanda.vehicle.domain.VehicleType;

public record VehicleSimulatorDTO(
        long driver_id,
        VehicleType type,
        long device_id
)
{}

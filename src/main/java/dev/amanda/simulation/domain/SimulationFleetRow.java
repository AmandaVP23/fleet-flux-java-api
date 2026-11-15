package dev.amanda.simulation.domain;

import dev.amanda.vehicle.domain.VehicleType;

public record SimulationFleetRow(
        Long vehicleId,
        Long deviceId,
        Long driverId,
        VehicleType type
) {}

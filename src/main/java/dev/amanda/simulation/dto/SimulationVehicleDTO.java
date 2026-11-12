package dev.amanda.simulation.dto;

import dev.amanda.vehicle.domain.VehicleType;

public record SimulationVehicleDTO(
        Long id,
        VehicleType type
) {}

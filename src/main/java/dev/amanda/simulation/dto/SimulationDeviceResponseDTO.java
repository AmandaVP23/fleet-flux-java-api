package dev.amanda.simulation.dto;

public record SimulationDeviceResponseDTO(
        Long deviceId,
        Long driverId, // null when the vehicle has no active driver
        SimulationVehicleDTO vehicle
) {}

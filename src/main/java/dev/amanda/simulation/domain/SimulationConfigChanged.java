package dev.amanda.simulation.domain;

public record SimulationConfigChanged(Type type, Long id) {
    public enum Type { DEVICE_ASSIGNED, ROUTE_CREATED }
}

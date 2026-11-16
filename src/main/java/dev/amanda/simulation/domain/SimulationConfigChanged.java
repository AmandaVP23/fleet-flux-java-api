package dev.amanda.simulation.domain;

public record SimulationConfigChanged(Type type, Long id) {
    public enum Type { UPDATE_DEVICES, ROUTE_CREATED }
}

package dev.amanda.simulation.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimulationConfigChanged(Type type, Long id) {
    public enum Type { UPDATE_DEVICES, ROUTE_CREATED }

    /** The simulator just re-fetches the whole device list, so this event carries no id. */
    public static SimulationConfigChanged updateDevices() {
        return new SimulationConfigChanged(Type.UPDATE_DEVICES, null);
    }
}

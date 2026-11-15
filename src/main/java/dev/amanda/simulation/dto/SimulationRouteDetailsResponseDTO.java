package dev.amanda.simulation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SimulationRouteDetailsResponseDTO {
    private Long routeId;
    private String geometry;
    private int geometryPrecision;
    private List<Double> segmentDistancesInMeters;
    private List<Double> segmentDurationsInSeconds;
}

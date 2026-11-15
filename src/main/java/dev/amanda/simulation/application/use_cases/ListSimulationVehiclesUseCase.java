package dev.amanda.simulation.application.use_cases;

import dev.amanda.simulation.domain.SimulationFleetRepository;
import dev.amanda.simulation.dto.SimulationDeviceResponseDTO;
import dev.amanda.simulation.dto.SimulationVehicleDTO;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;

import java.util.List;
import lombok.RequiredArgsConstructor;

@Log
@ApplicationScoped
@RequiredArgsConstructor
public class ListSimulationVehiclesUseCase {

    private final SimulationFleetRepository simulationFleetRepository;

    public List<SimulationDeviceResponseDTO> execute() {
        return simulationFleetRepository.getSimulationFleet().stream()
                .map(row -> new SimulationDeviceResponseDTO(
                        row.deviceId(),
                        row.driverId(),
                        new SimulationVehicleDTO(row.vehicleId(), row.type())
                ))
                .toList();
    }
}

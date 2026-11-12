package dev.amanda.simulation.application.use_cases;

import dev.amanda.simulation.domain.SimulationFleetRepository;
import dev.amanda.simulation.dto.SimulationDeviceResponseDTO;
import dev.amanda.simulation.dto.SimulationVehicleDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.java.Log;

import java.util.List;

@Log
@ApplicationScoped
public class ListSimulationVehiclesUseCase {

    @Inject
    SimulationFleetRepository simulationFleetRepository;

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

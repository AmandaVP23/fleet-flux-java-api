package dev.amanda.simulation.application.use_cases;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;

@Log
@ApplicationScoped
public class ListSimulationVehiclesUseCase {
}


//SELECT vehicle.id as vehicle_id, vehicle_device_assignment.device_id as device_id, vehicle_driver_assignment.driver_id as driver_id, type
//FROM vehicle
//INNER JOIN vehicle_device_assignment ON vehicle.id = vehicle_device_assignment.id
//LEFT JOIN vehicle_driver_assignment ON vehicle.id = vehicle_driver_assignment.id
//WHERE vehicle.status = 'ACTIVE';
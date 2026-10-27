package dev.amanda.vehicle_device_assignment.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class VehicleDeviceAssignmentRepository implements PanacheRepository<VehicleDeviceAssignment> {
    public Optional<VehicleDeviceAssignment> findActiveByVehicleAndDevice(long vehicleId, long deviceId) {
        return find("vehicle.id = ?1 and device.id = ?2 and deletedAt IS NULL", vehicleId, deviceId).firstResultOptional();
    }

    public Optional<VehicleDeviceAssignment> findActiveByVehicle(long vehicleId) {
        return find("vehicle.id = ?1 and deletedAt IS NULL", vehicleId).firstResultOptional();
    }

    public Optional<VehicleDeviceAssignment> findActiveByDevice(long deviceId) {
        return find("device.id = ?1 and deletedAt IS NULL", deviceId).firstResultOptional();
    }
}

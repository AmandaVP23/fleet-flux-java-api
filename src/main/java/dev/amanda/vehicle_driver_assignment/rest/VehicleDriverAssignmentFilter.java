package dev.amanda.vehicle_driver_assignment.rest;


import java.time.Instant;

public record VehicleDriverAssignmentFilter(Long organizationId, Long driverId, Long vehicleId, Instant from, Instant to) {
    public VehicleDriverAssignmentFilter withOrganizationId(Long organizationId) {
        return new VehicleDriverAssignmentFilter(organizationId, driverId, vehicleId, from, to);
    }
}

package dev.amanda.vehicle_driver_assignment.rest;


public record VehicleDriverAssignmentFilter(Long organizationId, Long driverId, Long vehicleId) {
    // todo - start date - end date
    public VehicleDriverAssignmentFilter withOrganizationId(Long organizationId) {
        return new VehicleDriverAssignmentFilter(organizationId, driverId, vehicleId);
    }
}

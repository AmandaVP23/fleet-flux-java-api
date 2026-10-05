package dev.amanda.vehicle_driver_assignment.domain;

import dev.amanda.vehicle_driver_assignment.rest.VehicleDriverAssignmentFilter;
import io.quarkus.panache.common.Sort;

import java.util.List;

public interface VehicleDriverAssignmentRepository {
    List<VehicleDriverAssignment> findPaginated(int page, int size, Sort sort, VehicleDriverAssignmentFilter filter);

    long count(VehicleDriverAssignmentFilter filter);
}

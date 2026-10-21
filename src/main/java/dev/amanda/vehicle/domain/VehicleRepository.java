package dev.amanda.vehicle.domain;

import dev.amanda.vehicle.applications.filters.VehicleFilter;
import io.quarkus.panache.common.Sort;

import java.util.List;

public interface VehicleRepository {
    Vehicle findByIdOrThrow(long id);

    Vehicle findByIdInOrganizationOrThrow(long id, long organizationId);

    List<Vehicle> listPaginated(VehicleFilter filter, int page, int size, Sort sort);

    long countWithQuery(VehicleFilter filter);
}

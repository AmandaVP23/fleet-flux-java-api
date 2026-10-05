package dev.amanda.vehicle_driver_assignment.persistence;

import dev.amanda.infrastructure.shared.application.QueryData;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignment;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignmentRepository;
import dev.amanda.vehicle_driver_assignment.rest.VehicleDriverAssignmentFilter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class VehicleDriverAssignmentRepositoryPanache implements VehicleDriverAssignmentRepository, PanacheRepository<VehicleDriverAssignment> {

    @Override
    public List<VehicleDriverAssignment> findPaginated(int page, int size, Sort sort, VehicleDriverAssignmentFilter filter) {
        QueryData queryData = buildQuery(filter);

        return find(queryData.query(), sort, queryData.params())
                .page(Page.of(page, size))
                .list();
    }

    @Override
    public long count(VehicleDriverAssignmentFilter filter) {
        QueryData queryData = buildQuery(filter);

        return count(queryData.query(), queryData.params());
    }

    private QueryData buildQuery(VehicleDriverAssignmentFilter filter) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (filter.driverId() != null) {
            query.append(" and driver.id = :driverId");
            params.put("driverId", filter.driverId());
        }

        if (filter.vehicleId() != null) {
            query.append(" and vehicle.id = :vehicleId");
            params.put("vehicleId", filter.vehicleId());
        }

        if (filter.organizationId() != null) {
            query.append(" and vehicle.organization.id = :organizationId");
            params.put("organizationId", filter.organizationId());
        }

        return new QueryData(query.toString(), params);
    }
}

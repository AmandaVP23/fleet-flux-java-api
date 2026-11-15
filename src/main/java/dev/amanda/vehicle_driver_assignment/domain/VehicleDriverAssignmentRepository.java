package dev.amanda.vehicle_driver_assignment.domain;

import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.vehicle.dto.VehicleDriverDTO;
import dev.amanda.vehicle_driver_assignment.rest.VehicleDriverAssignmentFilter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class VehicleDriverAssignmentRepository implements PanacheRepository<VehicleDriverAssignment> {

    @Inject
    EntityManager em;

    public List<VehicleDriverAssignment> findPaginated(int page, int size, Sort sort, VehicleDriverAssignmentFilter filter) {
        Filter f = buildFilter(filter);

        return find(f.where(), sort, f.params())
                .page(Page.of(page, size))
                .list();
    }

    public VehicleDriverAssignment findByIdInOrganizationOrThrow(long id, long organizationId) {
        return find("id = ?1 AND vehicle.organization.id = ?2", id, organizationId)
                .firstResultOptional()
                .orElseThrow(() -> new BaseApiException(ApiError.VEHICLE_DRIVER_ASSIGNMENT_NOT_FOUND));
    }

    public long count(VehicleDriverAssignmentFilter filter) {
        Filter f = buildFilter(filter);

        return count(f.where(), f.params());
    }

    public boolean verifyVehicleAssignmentHasConflict(
            long vehicleId,
            Instant startDateTime,
            Instant endDateTime
    ) {
        // todo - when editing exclude own id
        return verifyAssignmentHasConflict(
                "vehicle.id",
                vehicleId,
                startDateTime,
                endDateTime
        );
    }

    public boolean verifyDriverAssignmentHasConflict(
            long driverId,
            Instant startDateTime,
            Instant endDateTime
    ) {
        // todo - when editing exclude own id
        return verifyAssignmentHasConflict(
                "driver.id",
                driverId,
                startDateTime,
                endDateTime
        );
    }

    public Map<Long, VehicleDriverDTO> findActiveDriverIdsByVehicleIds(Collection<Long> vehicleIds) {
        if (vehicleIds.isEmpty()) return Map.of();

        Map<Long, VehicleDriverDTO> result = em.createQuery("""
            select vdr.vehicle.id, vdr.driver.id, vdr.driver.firstName,  vdr.driver.lastName
            from VehicleDriverAssignment vdr
            where vdr.vehicle.id in :ids and vdr.startDateTime <= :now and (vdr.endDateTime is null or vdr.endDateTime > :now)
        """, Object[].class)
                .setParameter("ids", vehicleIds)
                .setParameter("now", Instant.now())
                .getResultStream()
                .collect(Collectors.toMap(r -> (Long) r[0], r -> new VehicleDriverDTO((Long) r[1], (String) r[2], (String) r[3])));

        return result;
    }

    public VehicleDriverAssignment findActiveAtByOrganization(Instant at, Long vehicleId, Long driverId, long organizationId) {
        List<String> conditions = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        if (vehicleId != null) {
            conditions.add("vehicle.id = :vehicleId");
            params.put("vehicleId", vehicleId);

            conditions.add("vehicle.organization.id = :organizationId");
            params.put("organizationId", organizationId);
        }

        if (driverId != null) {
            conditions.add("driver.id = :driverId");
            params.put("driverId", driverId);
        }

        conditions.add("startDateTime <= :at AND (endDateTime IS NULL OR endDateTime > :at)");
        params.put("at", at);

        return find(String.join(" and ", conditions), params)
                .firstResultOptional()
                .orElseThrow(() -> new BaseApiException(ApiError.VEHICLE_DRIVER_ASSIGNMENT_NOT_FOUND));
    }

    private boolean verifyAssignmentHasConflict(
            String resourceField,
            long resourceId,
            Instant startDateTime,
            Instant endDateTime
    ) {
        List<String> conditions = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        conditions.add(resourceField + " = :resourceId");
        params.put("resourceId", resourceId);

        conditions.add("(endDateTime IS NULL OR endDateTime > :startDateTime)");
        params.put("startDateTime", startDateTime);

        if (endDateTime != null) {
            conditions.add(":endDateTime > startDateTime");
            params.put("endDateTime", endDateTime);
        }

        return count(String.join(" and ", conditions), params) > 0;
    }

    private Filter buildFilter(VehicleDriverAssignmentFilter filter) {
        List<String> conditions = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        if (filter.driverId() != null) {
            conditions.add("driver.id = :driverId");
            params.put("driverId", filter.driverId());
        }

        if (filter.vehicleId() != null) {
            conditions.add("vehicle.id = :vehicleId");
            params.put("vehicleId", filter.vehicleId());
        }

        if (filter.organizationId() != null) {
            conditions.add("vehicle.organization.id = :organizationId");
            params.put("organizationId", filter.organizationId());
        }

        if (filter.from() != null) {
            conditions.add("from >= :from");
            params.put("from", filter.from());
        }

        if (filter.to() != null) {
            conditions.add("to <= :to");
            params.put("to", filter.to());
        }

        // Panache treats a query without "from" as the where clause; empty means no filter
        return new Filter(String.join(" and ", conditions), params);
    }

    private record Filter(String where, Map<String, Object> params) {
    }
}

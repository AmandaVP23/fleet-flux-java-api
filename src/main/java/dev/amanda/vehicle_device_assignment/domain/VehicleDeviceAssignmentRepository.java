package dev.amanda.vehicle_device_assignment.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class VehicleDeviceAssignmentRepository implements PanacheRepository<VehicleDeviceAssignment> {

    @Inject
    EntityManager em;

    public Optional<VehicleDeviceAssignment> findActiveByVehicleAndDevice(long vehicleId, long deviceId) {
        return find("vehicle.id = ?1 and device.id = ?2 and deletedAt IS NULL", vehicleId, deviceId).firstResultOptional();
    }

    public Optional<VehicleDeviceAssignment> findActiveByVehicle(long vehicleId) {
        return find("vehicle.id = ?1 and deletedAt IS NULL", vehicleId).firstResultOptional();
    }

    public Optional<VehicleDeviceAssignment> findActiveByDevice(long deviceId) {
        return find("device.id = ?1 and deletedAt IS NULL", deviceId).firstResultOptional();
    }

    public Map<Long, Long> findActiveDeviceIdsByVehicleIds(Collection<Long> vehicleIds) {
        Map<Long, Long> result = em.createQuery("""
            select vda.vehicle.id, vda.device.id
            from VehicleDeviceAssignment vda
            where vda.vehicle.id in :ids and vda.deletedAt is null
        """, Object[].class)
                .setParameter("ids", vehicleIds)
                .getResultStream()
                .collect(Collectors.toMap(r -> (Long) r[0], r -> (Long) r[1]));

        return result;
    }
}

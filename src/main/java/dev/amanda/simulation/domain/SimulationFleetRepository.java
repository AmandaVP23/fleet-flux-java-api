package dev.amanda.simulation.domain;

import dev.amanda.vehicle.domain.VehicleStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class SimulationFleetRepository {

    @Inject
    EntityManager em;

    public List<SimulationFleetRow> getSimulationFleet() {
        TypedQuery<SimulationFleetRow> query = em.createQuery("""
                select v.id, vda.device.id, vdr.driver.id, v.type
                from VehicleDeviceAssignment vda
                join vda.vehicle v
                left join VehicleDriverAssignment vdr
                    on vdr.vehicle = v
                    and vdr.startDateTime <= :now
                    and (vdr.endDateTime is null or vdr.endDateTime > :now)
                where v.status = :status
                order by v.id
                """, SimulationFleetRow.class)
                .setParameter("now", Instant.now())
                .setParameter("status", VehicleStatus.ACTIVE);

        return query.getResultList();
    }
}

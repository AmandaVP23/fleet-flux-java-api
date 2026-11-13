package dev.amanda.simulation.domain;

import dev.amanda.route.domain.*;
import dev.amanda.vehicle.domain.VehicleStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SimulationFleetRepository {

    @Inject
    EntityManager em;

    @Inject
    RouteRepository routeRepository;

    @Inject
    RouteDetailsRepository routeDetailsRepository;

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

    public List<Route> getSimulationRoutes() {
        return routeRepository.find("from Route r join fetch r.vehicle where r.status = ?1", RouteStatus.SCHEDULED).list();
    }

    public Optional<RouteDetailsEntity> getSimulationRouteDetails(long routeId) {
        Optional<RouteDetailsEntity> routeDetails = routeDetailsRepository
                .find("id = ?1 AND route.status = ?2", routeId, RouteStatus.SCHEDULED)
                .firstResultOptional();

        return routeDetails;
    }
}

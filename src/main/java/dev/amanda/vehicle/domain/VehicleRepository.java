package dev.amanda.vehicle.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class VehicleRepository implements PanacheRepository<Vehicle> {
    public List<Vehicle> listPaginated(Long organizationId, int page, int size, Sort sort) {
        Map<String, Object> params = new HashMap<>();

        String query = buildQuery(organizationId, params);

        return find(query, sort, params)
                .page(Page.of(page, size))
                .list();

    }

    public long countWithQuery(Long organizationId) {

        Map<String, Object> params = new HashMap<>();

        String query = buildQuery(organizationId, params);

        return count(query, params);
    }

    private String buildQuery(Long organizationId, Map<String, Object> params) {

        String query = "1=1";

        if (organizationId != null) {
            query += " and organizationId = :orgId";
            params.put("orgId", organizationId);
        }

        return query;
    }
}

package dev.amanda.vehicle.domain;

import dev.amanda.vehicle.exceptions.VehicleNotFoundException;
import dev.amanda.vehicle.applications.filters.VehicleFilter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class VehicleRepository implements PanacheRepository<Vehicle> {

    public Vehicle findByIdOrThrow(long id) {
        return findByIdOptional(id)
                .orElseThrow(VehicleNotFoundException::new);
    }

    public Vehicle findByIdInOrganizationOrThrow(long id, long organizationId) {
        return find("from Vehicle v left join fetch v.brand left join fetch v.organization " +
                "where v.id = ?1 and v.organization.id = ?2", id, organizationId)
                .firstResultOptional()
                .orElseThrow(VehicleNotFoundException::new);
    }

    public List<Vehicle> listPaginated(VehicleFilter filter, int page, int size, Sort sort) {
        Filter f = buildFilter(filter);

        return find("from Vehicle v " +
                "left join fetch v.brand " +
                "left join fetch v.organization" +
                f.where(), sort, f.params())
                .page(Page.of(page, size))
                .list();
    }

    public long countWithQuery(VehicleFilter filter) {
        Filter f = buildFilter(filter);

        return count("from Vehicle v" + f.where(), f.params());
    }

    private Filter buildFilter(VehicleFilter filter) {
        List<String> conditions = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        if (filter.organizationId != null) {
            conditions.add("v.organization.id = :orgId");
            params.put("orgId", filter.organizationId);
        }

        if (filter.brandId != null) {
            conditions.add("v.brand.id = :brandId");
            params.put("brandId", filter.brandId);
        }

        String where = conditions.isEmpty() ? "" : " where " + String.join(" and ", conditions);
        return new Filter(where, params);
    }

    private record Filter(String where, Map<String, Object> params) {
    }
}

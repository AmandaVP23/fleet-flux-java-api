package dev.amanda.vehicle.persistence;

import dev.amanda.infrastructure.shared.application.QueryData;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle.exceptions.VehicleNotFoundException;
import dev.amanda.vehicle.applications.filters.VehicleFilter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class VehicleRepositoryPanache implements VehicleRepository, PanacheRepository<Vehicle> {

    @Override
    public Vehicle findByIdOrThrow(long id) {
        return findByIdOptional(id)
                .orElseThrow(VehicleNotFoundException::new);
    }

    @Override
    public List<Vehicle> listPaginated(VehicleFilter filter, int page, int size, Sort sort) {
        QueryData queryData = buildQuery(filter);

        return find(queryData.query(), sort, queryData.params())
                .page(Page.of(page, size))
                .list();
    }

    @Override
    public long countWithQuery(VehicleFilter filter) {
        QueryData queryData = buildQuery(filter);

        return count(queryData.query(), queryData.params());
    }

    private QueryData buildQuery(VehicleFilter filter) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (filter.organizationId != null) {
            query.append(" and organization.id = :orgId");
            params.put("orgId", filter.organizationId);
        }

        if (filter.brandId != null) {
            query.append(" and brand.id = :brandId");
            params.put("brandId", filter.brandId);
        }

        return new QueryData(query.toString(), params);
    }
}

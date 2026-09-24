package dev.amanda.organization.domain;

import dev.amanda.organization.dto.OrganizationStatusFilter;
import dev.amanda.organization.exceptions.OrganizationNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrganizationRepository implements PanacheRepository<Organization> {

    private static final String ACTIVE_FILTER = "deletedAt IS NULL";
    private static final String DELETED_FILTER = "deletedAt IS NOT NULL";

    public Optional<Organization> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public Optional<Organization> findByHostname(String hostname) {
        return find("hostname", hostname).firstResultOptional();
    }

    public Optional<Organization> findByRealm(String realm) {
        return find("realm", realm).firstResultOptional();
    }

    public List<Organization> findPaginated(
            int page,
            int size,
            Sort sort,
            OrganizationStatusFilter filter
    ) {
        PanacheQuery<Organization> query = switch (filter) {
            case ALL -> findAll(sort);
            case ACTIVE -> find(ACTIVE_FILTER, sort);
            case DELETED -> find(DELETED_FILTER, sort);
        };

        return query
                .page(Page.of(page, size))
                .list();
    }

    public long count(OrganizationStatusFilter filter) {
        return switch (filter) {
            case ALL -> count();
            case ACTIVE -> count(ACTIVE_FILTER);
            case DELETED -> count(DELETED_FILTER);
        };
    }

    public Organization findActiveByIdOrThrow(long id) {
        return findByIdOptional(id)
                .orElseThrow(OrganizationNotFoundException::new);
    }

    public Organization findByIdOrThrow(Long id) {
        return find(
                "id = ?1 and deletedAt IS NULL",
                id
        ).firstResultOptional()
                .orElseThrow(OrganizationNotFoundException::new);
    }
}

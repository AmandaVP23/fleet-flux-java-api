package dev.amanda.organization.persistence;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.application.filters.OrganizationStatusFilter;
import dev.amanda.organization.exceptions.OrganizationNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrganizationRepositoryPanache implements OrganizationRepository, PanacheRepository<Organization> {

    private static final String ACTIVE_FILTER = "deletedAt IS NULL";
    private static final String DELETED_FILTER = "deletedAt IS NOT NULL";

    @Override
    public Optional<Organization> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    @Override
    public Optional<Organization> findByHostname(String hostname) {
        return find("hostname", hostname).firstResultOptional();
    }

    @Override
    public Optional<Organization> findByRealm(String realm) {
        return find("realm", realm).firstResultOptional();
    }

    @Override
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

    @Override
    public long count(OrganizationStatusFilter filter) {
        return switch (filter) {
            case ALL -> count();
            case ACTIVE -> count(ACTIVE_FILTER);
            case DELETED -> count(DELETED_FILTER);
        };
    }

    @Override
    public Organization findByIdOrThrow(long id) {
        return findByIdOptional(id)
                .orElseThrow(OrganizationNotFoundException::new);
    }

    @Override
    public Organization findActiveByIdOrThrow(Long id) {
        return find(
                "id = ?1 and deletedAt IS NULL",
                id
        ).firstResultOptional()
                .orElseThrow(OrganizationNotFoundException::new);
    }
}

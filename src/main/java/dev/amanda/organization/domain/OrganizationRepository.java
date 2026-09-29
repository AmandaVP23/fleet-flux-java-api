package dev.amanda.organization.domain;

import dev.amanda.organization.application.filters.OrganizationStatusFilter;
import io.quarkus.panache.common.Sort;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository {
    Optional<Organization> findByName(String name);

    Optional<Organization> findByHostname(String hostname);

    Optional<Organization> findByRealm(String realm);

    List<Organization> findPaginated(int page, int size, Sort sort, OrganizationStatusFilter statusFilter);

    long count(OrganizationStatusFilter statusFilter);

    Organization findActiveByIdOrThrow(long id);

    Organization findByIdOrThrow(Long id);
}

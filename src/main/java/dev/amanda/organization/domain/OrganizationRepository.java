package dev.amanda.organization.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrganizationRepository implements PanacheRepository<Organization> {
    public Optional<Organization> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public Optional<Organization> findByRealm(String realm) {
        return find("realm", realm).firstResultOptional();
    }

    public List<Organization> findPaginated(int page, int size, Sort sort) {
        return findAll(sort)
                .page(Page.of(page, size))
                .list();
    }

    public long countAll() {
        return count();
    }
}

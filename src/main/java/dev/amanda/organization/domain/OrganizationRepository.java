package dev.amanda.organization.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class OrganizationRepository implements PanacheRepository<Organization> {
    public Optional<Organization> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public Optional<Organization> findByRealm(String realm) {
        return find("realm", realm).firstResultOptional();
    }
}

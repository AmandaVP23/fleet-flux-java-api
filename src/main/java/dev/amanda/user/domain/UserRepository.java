package dev.amanda.user.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public Optional<User> findByEmail(String email){
        return find("email", email).firstResultOptional();
    }

    public List<User> findUsersInOrganization(long organizationId){
        return find("organizationId", organizationId).list();
    }

    public List<User> findAllPaginated(int page, int size, Sort sort) {
        return findAll(sort)
                .page(Page.of(page, size))
                .list();
    }

    public List<User> findPaginatedByOrganization(Long orgId, int page, int size, Sort sort) {
        return find("organization.id", orgId)
                .page(Page.of(page, size))
                .list();
    }

    public long countAll() {
        return count();
    }

    public long countByOrganization(Long orgId) {
        return count("organizationId", orgId);
    }
}

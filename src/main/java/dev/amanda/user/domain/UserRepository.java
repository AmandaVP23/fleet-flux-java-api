package dev.amanda.user.domain;

import dev.amanda.user.exceptions.UserNotFoundException;
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

    // todo - return users by organization
    public List<User> findUsersInOrganization(long organizationId){
        return find("organization.id", organizationId).list();
    }

    public List<User> findAllPaginated(int page, int size, Sort sort) {
        return findAll(sort)
                .page(Page.of(page, size))
                .list();
    }

    public List<User> findPaginatedByOrganization(Long orgId, int page, int size, Sort sort) {
        // todo - sort
        return find("organization.id", orgId)
                .page(Page.of(page, size))
                .list();
    }

    public long countAll() {
        return count();
    }

    public long countByOrganization(Long orgId) {
        return count("organization.id", orgId);
    }

    public Optional<User> findByKeycloakId(String keycloakId) {
        return find("keycloakId", keycloakId).firstResultOptional();
    }

    public User findByKeycloakIdOrThrow(String keycloakId) {
        Optional<User> user = this.findByKeycloakId(keycloakId);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        return user.get();
    }

    public User findByIdOrThrow(long id) {
        return findByIdOptional(id)
                .orElseThrow(UserNotFoundException::new);
    }
}

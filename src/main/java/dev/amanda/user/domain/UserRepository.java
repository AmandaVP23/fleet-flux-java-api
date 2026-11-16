package dev.amanda.user.domain;

import dev.amanda.user.exceptions.UserNotFoundException;
import dev.amanda.user.rest.UserFilter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByEmail(String email){
        return find("email", email).firstResultOptional();
    }

    public List<User> findPaginated(int page, int size, Sort sort, UserFilter filter) {
        Filter f = buildFilter(filter);

        // Qualify sort columns with the root alias: organization also has id/createdAt (BaseEntity)
        Sort qualifiedSort = Sort.empty().disableEscaping();
        sort.getColumns().forEach(c -> qualifiedSort.and("u." + c.getName(), c.getDirection()));

        return find("from User u left join fetch u.organization" + f.where(), qualifiedSort, f.params())
                .page(Page.of(page, size))
                .list();
    }

    public long count(UserFilter filter) {
        Filter f = buildFilter(filter);

        return count("from User u" + f.where(), f.params());
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

    public User findByIdInOrganizationOrThrow(long id, long organizationId) {
        return find("from User u left join fetch u.organization " +
                "where u.id = ?1 and u.organization.id = ?2", id, organizationId)
                .firstResultOptional()
                .orElseThrow(UserNotFoundException::new);
    }

    private Filter buildFilter(UserFilter filter) {
        List<String> conditions = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        if (filter.organizationId() != null) {
            conditions.add("u.organization.id = :orgId");
            params.put("orgId", filter.organizationId());
        }

        if (filter.role() != null) {
            conditions.add("u.role = :role");
            params.put("role", filter.role());
        }

        String where = conditions.isEmpty() ? "" : " where " + String.join(" and ", conditions);
        return new Filter(where, params);
    }

    private record Filter(String where, Map<String, Object> params) {
    }
}

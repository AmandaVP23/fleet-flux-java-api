package dev.amanda.user.persistence;

import dev.amanda.infrastructure.shared.application.QueryData;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.user.exceptions.UserNotFoundException;
import dev.amanda.user.rest.UserFilter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryPersistence implements UserRepository, PanacheRepository<User> {
    @Override
    public Optional<User> findByEmail(String email){
        return find("email", email).firstResultOptional();
    }

    @Override
    public List<User> findPaginated(int page, int size, Sort sort, UserFilter filter) {
        QueryData queryData = buildQuery(filter);

        return find(queryData.query(), sort, queryData.params())
                .page(Page.of(page, size))
                .list();
    }

    @Override
    public long count(UserFilter filter) {
        QueryData queryData = buildQuery(filter);

        return count(queryData.query(), queryData.params());
    }

    @Override
    public Optional<User> findByKeycloakId(String keycloakId) {
        return find("keycloakId", keycloakId).firstResultOptional();
    }

    @Override
    public User findByKeycloakIdOrThrow(String keycloakId) {
        Optional<User> user = this.findByKeycloakId(keycloakId);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        return user.get();
    }

    @Override
    public User findByIdOrThrow(long id) {
        return findByIdOptional(id)
                .orElseThrow(UserNotFoundException::new);
    }

    private QueryData buildQuery(UserFilter filter) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (filter.organizationId() != null) {
            query.append(" and organization.id = :orgId");
            params.put("orgId", filter.organizationId());
        }

        if (filter.role() != null) {
            query.append(" and role = :role");
            params.put("role", filter.role());
        }

        return new QueryData(query.toString(), params);
    }
}

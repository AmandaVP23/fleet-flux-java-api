package dev.amanda.user.domain;

import dev.amanda.user.rest.UserFilter;
import io.quarkus.panache.common.Sort;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    List<User> findPaginated(int page, int size, Sort sort, UserFilter filter);

    long count(UserFilter filter);

    Optional<User> findByKeycloakId(String keycloakId);

    User findByKeycloakIdOrThrow(String keycloakId);

    User findByIdOrThrow(long id);
}

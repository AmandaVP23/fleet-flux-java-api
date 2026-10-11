package dev.amanda.user.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.oidc.KeycloakAdmin;
import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.user.domain.Role;
import dev.amanda.user.domain.User;
import dev.amanda.user.persistence.UserRepositoryPersistence;
import dev.amanda.user.exceptions.UserNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.Objects;

@ApplicationScoped
public class DeleteUserUseCase {

    @Inject
    UserRepositoryPersistence userRepositoryPersistence;

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Transactional
    public void execute(long id, AuthContext authContext) {
        // todo - if organization will have no org admins left?

        User user = userRepositoryPersistence.findByIdOrThrow(id);

        if (Objects.equals(user.getKeycloakId(), authContext.getUserKeycloakId())) {
            throw new BaseApiException(ApiError.NOT_ALLOWED, "You are not allowed to delete yourself");
        }

        long userOrgId = user.getOrganization().getId();

        if (!authContext.isSuperAdmin() && authContext.getOrganizationId() != null && userOrgId != authContext.getOrganizationId()) {
            throw new UserNotFoundException();
        }

        if (!authContext.isSuperAdmin() && user.getRole() == Role.ORG_ADMIN) {
            throw new BaseApiException(ApiError.NOT_ALLOWED);
        }

        if (user.getDeletedAt() != null) {
            throw new UserNotFoundException();
        }

        String orgRealm = user.getOrganization().getRealm();

        keycloakAdmin.changeUserEnableState(orgRealm, user.getKeycloakId(), false);

        user.setDeletedAt(Instant.now());
        userRepositoryPersistence.persist(user);
    }
}

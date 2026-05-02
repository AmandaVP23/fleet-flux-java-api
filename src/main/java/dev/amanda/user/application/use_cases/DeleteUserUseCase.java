package dev.amanda.user.application.use_cases;

import dev.amanda.oidc.AuthContext;
import dev.amanda.oidc.KeycloakAdmin;
import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;
import dev.amanda.user.domain.Role;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.user.exceptions.UserNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;

@ApplicationScoped
public class DeleteUserUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Transactional
    public void execute(long id, AuthContext authContext) {
        // todo - if organization will have no org admins left?
        User user = userRepository.findByIdOrThrow(id);

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
        userRepository.persist(user);
    }
}

package dev.amanda.organization.application;

import dev.amanda.oidc.KeycloakAdmin;
import dev.amanda.oidc.OrgTenantConfigResolver;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

import java.util.List;

@Log
@ApplicationScoped
public class HardDeleteOrganizationUseCase {
    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Inject
    OrgTenantConfigResolver tenantConfigResolver;

    @Transactional
    public void execute(long id) {
        Organization organization = organizationRepository.findByIdOrThrow(id);

        if (organization.getDeletedAt() == null) {
            throw new BaseApiException(ApiError.ORGANIZATION_NOT_INACTIVE, "Organization is not soft deleted");
        }

        // todo - come back here after more data

        // todo - try to understand if this can be a problem
//        List<User> users = userRepository.findUsersInOrganization(organization.getId());
//        for (User user : users) {
//            userRepository.delete(user);
//        }

        String realm = organization.getRealm();
        organizationRepository.delete(organization);

        keycloakAdmin.revokeAllSessions(realm);

        keycloakAdmin.deleteAllRealmUsers(realm);

        keycloakAdmin.deleteAllRealmClients(realm);

        keycloakAdmin.deleteRealm(realm);

        tenantConfigResolver.evict(realm);

        log.info("Hard delete organization " + organization.getId() + " name: " + organization.getName() + " realm: " + realm);
    }
}

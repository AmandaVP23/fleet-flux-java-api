package dev.amanda.organization.application.use_cases;

import dev.amanda.infrastructure.oidc.KeycloakAdmin;
import dev.amanda.infrastructure.oidc.OrgTenantConfigResolver;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.persistence.OrganizationRepositoryPanache;
import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

import java.time.Instant;

@Log
@ApplicationScoped
public class SoftDeleteOrganizationUseCase {

    @Inject
    OrganizationRepositoryPanache organizationRepositoryPanache;

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Inject
    OrgTenantConfigResolver tenantConfigResolver;

    // TODO wrap Keycloak calls with retry (e.g., 3 attempts)
    @Transactional
    public void execute(long id) {
        Organization organization = organizationRepositoryPanache.findActiveByIdOrThrow(id);

        if (organization.getDeletedAt() != null) {
            throw new BaseApiException(ApiError.ORGANIZATION_INACTIVE, "Organization was already deleted");
        }

        organization.setDeletedAt(Instant.now());
        organizationRepositoryPanache.persist(organization);

        String realm = organization.getRealm();

        keycloakAdmin.revokeAllSessions(realm);

        // changing users and clients enable state is not really needed but it's better
        keycloakAdmin.changeRealmUsersEnableState(realm, false);

        keycloakAdmin.changeRealmClientsEnableState(realm, false);

        keycloakAdmin.changeRealmEnableState(realm, false);

        tenantConfigResolver.evict(realm);

        log.info("Deleted organization with id " + id);
    }
}

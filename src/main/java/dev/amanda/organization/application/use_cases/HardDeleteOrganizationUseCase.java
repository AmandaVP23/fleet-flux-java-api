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

@Log
@ApplicationScoped
public class HardDeleteOrganizationUseCase {
    @Inject
    OrganizationRepositoryPanache organizationRepositoryPanache;

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Inject
    OrgTenantConfigResolver tenantConfigResolver;

    @Transactional
    public void execute(long id) {
        Organization organization = organizationRepositoryPanache.findActiveByIdOrThrow(id);

        if (organization.getDeletedAt() == null) {
            throw new BaseApiException(ApiError.ORGANIZATION_NOT_INACTIVE, "Organization is not soft deleted");
        }

        // todo - come back here after more data
        String realm = organization.getRealm();
        organizationRepositoryPanache.delete(organization);

        keycloakAdmin.revokeAllSessions(realm);

        keycloakAdmin.deleteAllRealmUsers(realm);

        keycloakAdmin.deleteAllRealmClients(realm);

        keycloakAdmin.deleteRealm(realm);

        tenantConfigResolver.evict(realm);

        log.info("Hard delete organization " + organization.getId() + " name: " + organization.getName() + " realm: " + realm);
    }
}

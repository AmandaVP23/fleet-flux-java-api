package dev.amanda.organization.application.use_cases;

import dev.amanda.oidc.KeycloakAdmin;
import dev.amanda.oidc.OrgTenantConfigResolver;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

@Log
@ApplicationScoped
public class RestoreSoftDeletedOrganizationUseCase {
    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Inject
    OrgTenantConfigResolver tenantConfigResolver;

    @Transactional
    public void execute(long id) {
        Organization organization = organizationRepository.findByIdOrThrow(id);

        if (organization.getDeletedAt() == null) {
            throw new BaseApiException(ApiError.ORGANIZATION_NOT_INACTIVE, "Organization is not deleted");
        }

        organization.setDeletedAt(null);

        String realm =  organization.getRealm();

        keycloakAdmin.changeRealmUsersEnableState(realm, true);

        keycloakAdmin.changeRealmClientsEnableState(realm, true);

        keycloakAdmin.changeRealmEnableState(realm, true);

        tenantConfigResolver.evictAndReload(realm);

        log.info("Restored deleted organization with id " + id);
    }
}

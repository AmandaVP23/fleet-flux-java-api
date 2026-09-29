package dev.amanda.organization.application.use_cases;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.persistence.OrganizationRepositoryPanache;
import dev.amanda.organization.dto.UpdateOrganizationDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateOrganizationUseCase {

    @Inject
    OrganizationRepositoryPanache organizationRepositoryPanache;

    @Transactional
    public void execute(long id, UpdateOrganizationDTO updateOrganizationDTO) {
        Organization organization = organizationRepositoryPanache.findActiveByIdOrThrow(id);

        if (updateOrganizationDTO.name != null) {
            organization.setName(updateOrganizationDTO.name);
        }
    }
}

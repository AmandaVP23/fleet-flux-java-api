package dev.amanda.organization.application;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.UpdateOrganizationDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@ApplicationScoped
public class UpdateOrganizationUseCase {
    @Inject
    OrganizationRepository organizationRepository;

    @Transactional
    public void execute(long id, UpdateOrganizationDTO updateOrganizationDTO) {
        Organization organization = organizationRepository.findActiveByIdOrThrow(id);

        if (updateOrganizationDTO.name != null) {
            organization.setName(updateOrganizationDTO.name);
        }
    }
}

package dev.amanda.organization.application;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetOrganizationByIdUseCase {

    @Inject
    OrganizationRepository organizationRepository;

    public OrganizationResponseDTO execute(long id) {
        Organization organization = organizationRepository.findByIdOrThrow(id);

        return OrganizationResponseDTO.from(organization);
    }
}

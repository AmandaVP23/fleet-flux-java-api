package dev.amanda.organization.application.use_cases;

import dev.amanda.organization.application.mappers.OrganizationMapper;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.persistence.OrganizationRepositoryPanache;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetOrganizationByIdUseCase {

    @Inject
    OrganizationRepositoryPanache organizationRepositoryPanache;

    @Inject
    OrganizationMapper organizationMapper;

    public OrganizationResponseDTO execute(long id) {
        Organization organization = organizationRepositoryPanache.findByIdOrThrow(id);

        return organizationMapper.toDto(organization);
    }
}

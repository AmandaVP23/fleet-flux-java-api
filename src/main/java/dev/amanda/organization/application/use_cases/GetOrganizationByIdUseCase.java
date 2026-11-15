package dev.amanda.organization.application.use_cases;

import dev.amanda.organization.application.mappers.OrganizationMapper;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GetOrganizationByIdUseCase {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationResponseDTO execute(long id) {
        Organization organization = organizationRepository.findActiveByIdOrThrow(id);

        return organizationMapper.toDto(organization);
    }
}

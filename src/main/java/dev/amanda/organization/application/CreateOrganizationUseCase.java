package dev.amanda.organization.application;

import java.text.Normalizer;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.CreateOrganizationDTO;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateOrganizationUseCase {

    @Inject
    OrganizationRepository organizationRepository;

    public OrganizationResponseDTO execute(CreateOrganizationDTO createOrganizationDTO) {
        Organization existingOrg = organizationRepository.findByName(createOrganizationDTO.name).orElse(null);

        if (existingOrg != null) {
            throw new IllegalStateException("Organization with name " + createOrganizationDTO.name + " already exists");
        }

        Organization organization = new Organization();
        organization.setName(createOrganizationDTO.name);
        organization.setRealm(this.getOrganizationRealm(createOrganizationDTO.name));

        organizationRepository.persist(organization);

        return new OrganizationResponseDTO(
                organization.getId(),
                organization.getName()
        );
    }

    private String getOrganizationRealm(String orgName) {
        String cleaned = orgName.trim();
        // Replace spaces (one or more) with underscore
        cleaned = cleaned.replaceAll("\\s+", "_");
        // Normalize to remove accents/diacritics
        cleaned = Normalizer.normalize(cleaned, Normalizer.Form.NFD);
        cleaned = cleaned.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return cleaned;
    }
}

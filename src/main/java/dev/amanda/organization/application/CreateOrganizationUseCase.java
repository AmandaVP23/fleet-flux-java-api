package dev.amanda.organization.application;

import java.text.Normalizer;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.CreateOrganizationDTO;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.organization.exceptions.OrganizationAlreadyExistsException;
import dev.amanda.organization.exceptions.OrganizationWithSameRealmAlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateOrganizationUseCase {

    @Inject
    OrganizationRepository organizationRepository;

    public OrganizationResponseDTO execute(CreateOrganizationDTO createOrganizationDTO) {
        // todo trim name
        // todo test name "    " multiple spaces
        organizationRepository.findByName(createOrganizationDTO.name).ifPresent(org -> {
            throw new OrganizationAlreadyExistsException();
        });

        String realmValue = this.getOrganizationRealm(createOrganizationDTO.name);
        organizationRepository.findByRealm(realmValue).ifPresent(org -> {
            throw new OrganizationWithSameRealmAlreadyExistsException();
        });

        Organization organization = new Organization();
        organization.setName(createOrganizationDTO.name);
        organization.setRealm(realmValue);

        organizationRepository.persist(organization);

        return new OrganizationResponseDTO(
                organization.getId(),
                organization.getName()
        );
    }

    private String getOrganizationRealm(String orgName) {
        String normalized = Normalizer.normalize(orgName.trim(), Normalizer.Form.NFD);
        return normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "") // strip accents
                .replaceAll("[^a-zA-Z0-9\\s]", "")                  // remove punctuation/symbols
                .trim()                                              // clean up any leading/trailing spaces left behind
                .replaceAll("\\s+", "_")                            // collapse spaces to underscores
                .toLowerCase();
    }
}

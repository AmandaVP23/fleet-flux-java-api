package dev.amanda.organization.application;

import java.text.Normalizer;

import dev.amanda.oidc.KeycloakAdmin;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.CreateOrganizationDTO;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.organization.exceptions.OrganizationAlreadyExistsException;
import dev.amanda.organization.exceptions.OrganizationWithSameRealmAlreadyExistsException;
import dev.amanda.user.domain.Roles;
import dev.amanda.user.domain.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.keycloak.representations.idm.RealmRepresentation;

@ApplicationScoped
public class CreateOrganizationUseCase {

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Inject
    UserRepository userRepository;

    @Inject
    SaveOrganizationUseCase saveOrganizationUseCase;

    public OrganizationResponseDTO execute(CreateOrganizationDTO createOrganizationDTO) {
        String realmValue = this.getOrganizationRealm(createOrganizationDTO.name);
        String realmId = null;
        String userKeycloakId = null;

        organizationRepository.findByName(createOrganizationDTO.name).ifPresent(org -> {
            throw new OrganizationAlreadyExistsException();
        });

        organizationRepository.findByRealm(realmValue).ifPresent(org -> {
            throw new OrganizationWithSameRealmAlreadyExistsException();
        });

        try {
            RealmRepresentation realmRepresentation = keycloakAdmin.createRealm(realmValue, createOrganizationDTO.name);
            realmId = realmRepresentation.getId();
            userKeycloakId = keycloakAdmin.createRealmUser(realmValue, createOrganizationDTO.adminFirstName, createOrganizationDTO.adminLastName, createOrganizationDTO.adminEmail, Roles.ORG_ADMIN).getId();

            return saveOrganizationUseCase.execute(createOrganizationDTO, realmValue, userKeycloakId);
        } catch (Exception e) {
            if (userKeycloakId != null) {
                keycloakAdmin.deleteUser(realmValue, userKeycloakId);
            }

            if (realmId != null) {
                keycloakAdmin.deleteRealm(realmId);
            }

            System.err.println(e.getMessage());
            throw new RuntimeException("Failed to create organization", e);
        }
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

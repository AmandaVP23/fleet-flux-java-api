package dev.amanda.identity.application;

import dev.amanda.config.SuperAdminConfig;
import dev.amanda.identity.dto.KeycloakConfigRequestDTO;
import dev.amanda.identity.dto.KeycloakConfigResponseDTO;
import dev.amanda.oidc.KeycloakConfig;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.exceptions.OrganizationNotFoundException;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@ApplicationScoped
public class GetKeycloakConfigUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    KeycloakConfig keycloakConfig;

    @Inject
    SuperAdminConfig superAdminConfig;

    @ConfigProperty(name = "quarkus.keycloak.default-web-client-name")
    String defaultWebClientName;

    public KeycloakConfigResponseDTO execute(String slug) {
        KeycloakConfigResponseDTO.KeycloakConfigResponseDTOBuilder keycloakConfigResponseDTOBuilder = KeycloakConfigResponseDTO
                .builder()
                .clientId(defaultWebClientName)
                .serverUrl(keycloakConfig.serverUrl());

        if (slug.equalsIgnoreCase(superAdminConfig.slug())) {
            return keycloakConfigResponseDTOBuilder
                    .realm(superAdminConfig.realm())
                    .build();
        }

        Organization organization = organizationRepository.findBySlug(slug)
                .filter(org -> org.getDeletedAt() == null)
                .orElseThrow(OrganizationNotFoundException::new);

        return keycloakConfigResponseDTOBuilder
                .realm(organization.getRealm())
                .build();
    }
}

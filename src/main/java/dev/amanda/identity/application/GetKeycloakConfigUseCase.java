package dev.amanda.identity.application;

import dev.amanda.infrastructure.config.SuperAdminConfig;
import dev.amanda.identity.dto.KeycloakConfigResponseDTO;
import dev.amanda.infrastructure.oidc.KeycloakConfig;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.exceptions.OrganizationNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class GetKeycloakConfigUseCase {

    private final OrganizationRepository organizationRepository;
    private final KeycloakConfig keycloakConfig;
    private final SuperAdminConfig superAdminConfig;
    private final String defaultWebClientName;

    public GetKeycloakConfigUseCase(
            OrganizationRepository organizationRepository,
            KeycloakConfig keycloakConfig,
            SuperAdminConfig superAdminConfig,
            @ConfigProperty(name = "quarkus.keycloak.default-web-client-name") String defaultWebClientName) {
        this.organizationRepository = organizationRepository;
        this.keycloakConfig = keycloakConfig;
        this.superAdminConfig = superAdminConfig;
        this.defaultWebClientName = defaultWebClientName;
    }

    public KeycloakConfigResponseDTO execute(String hostname) {
        KeycloakConfigResponseDTO.KeycloakConfigResponseDTOBuilder keycloakConfigResponseDTOBuilder = KeycloakConfigResponseDTO
                .builder()
                .clientId(defaultWebClientName)
                .serverUrl(keycloakConfig.serverUrl());

        if (hostname.equalsIgnoreCase(superAdminConfig.hostname())) {
            return keycloakConfigResponseDTOBuilder
                    .realm(superAdminConfig.realm())
                    .build();
        }

        Organization organization = organizationRepository.findByHostname(hostname)
                .filter(org -> org.getDeletedAt() == null)
                .orElseThrow(OrganizationNotFoundException::new);

        return keycloakConfigResponseDTOBuilder
                .realm(organization.getRealm())
                .build();
    }
}

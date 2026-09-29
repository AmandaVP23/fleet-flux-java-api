package dev.amanda.identity.application;

import dev.amanda.infrastructure.config.SuperAdminConfig;
import dev.amanda.identity.dto.KeycloakConfigResponseDTO;
import dev.amanda.infrastructure.oidc.KeycloakConfig;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.persistence.OrganizationRepositoryPanache;
import dev.amanda.organization.exceptions.OrganizationNotFoundException;
import dev.amanda.user.persistence.UserRepositoryPersistence;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class GetKeycloakConfigUseCase {

    @Inject
    UserRepositoryPersistence userRepositoryPersistence;

    @Inject
    OrganizationRepositoryPanache organizationRepositoryPanache;

    @Inject
    KeycloakConfig keycloakConfig;

    @Inject
    SuperAdminConfig superAdminConfig;

    @ConfigProperty(name = "quarkus.keycloak.default-web-client-name")
    String defaultWebClientName;

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

        Organization organization = organizationRepositoryPanache.findByHostname(hostname)
                .filter(org -> org.getDeletedAt() == null)
                .orElseThrow(OrganizationNotFoundException::new);

        return keycloakConfigResponseDTOBuilder
                .realm(organization.getRealm())
                .build();
    }
}

package dev.amanda.oidc;

import dev.amanda.config.SuperAdminConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped
public class KeycloakAdmin {
    private Keycloak keycloak;

    @Inject
    KeycloakConfig keycloakConfig;

    @Inject
    SuperAdminConfig superAdminConfig;

    @PostConstruct
    void init() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(this.keycloakConfig.serverUrl())
                .realm(this.keycloakConfig.realm())
                .grantType(this.keycloakConfig.grantType())
                .clientId(this.keycloakConfig.clientId())
                .clientSecret(this.keycloakConfig.clientSecret())
                .build();
    }

    @PreDestroy
    void destroy() {
        this.keycloak.close();
    }

    public UserRepresentation createSuperAdminUser() {
        UserRepresentation user = getSuperAdminUserRepresentation();

        try (Response response = realm().users().create(user)) {
            if (response.getStatus() != 201) {
                String body = response.readEntity(String.class);
                System.out.println("Failed to create user: " + response.getStatus());
                System.out.println(body);
                // Creation failed
                throw new RuntimeException("Failed to create user: "
                        + response.getStatus() + " " + body);
            }

            System.out.println("User created successfully");
        }

        return getSuperAdminUser().orElseThrow(() -> new IllegalArgumentException("User was created but could not be retrieved"));
    }

    public Optional<UserRepresentation> getSuperAdminUser() {
        List<UserRepresentation> userRepresentationList = realm()
                .users()
                .searchByEmail(superAdminConfig.email(),  true);

        return userRepresentationList.stream().findFirst();
    }

    private RealmResource realm() {
        return this.keycloak.realm(keycloakConfig.realm());
    }

    private UserRepresentation getSuperAdminUserRepresentation() {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue(superAdminConfig.password());
        credentialRepresentation.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(superAdminConfig.username());
        user.setEmail(superAdminConfig.email());
        user.setEnabled(true);
        user.setEmailVerified(true);

        user.setCredentials(List.of(credentialRepresentation));
        return user;
    }
}

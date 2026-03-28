package dev.amanda.oidc;

import dev.amanda.config.SuperAdminConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
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

        try (Response response = adminRealm().users().create(user)) {
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

        UserRepresentation createdUser = getSuperAdminUser().orElseThrow(() -> new IllegalArgumentException("User was created but could not be retrieved"));

        RoleRepresentation role = getOrCreateRole(this.keycloakConfig.realm(), "super_admin");

        adminRealm()
                .users()
                .get(createdUser.getId())
                .roles()
                .realmLevel()
                .add(List.of(role));

        return getSuperAdminUser().orElseThrow(() -> new IllegalArgumentException("User was created, role was add but could not be retrieved"));
    }

    public Optional<UserRepresentation> getSuperAdminUser() {
        List<UserRepresentation> userRepresentationList = adminRealm()
                .users()
                .searchByEmail(superAdminConfig.email(),  true);

        return userRepresentationList.stream().findFirst();
    }

    private RealmResource adminRealm() {
        return this.keycloak.realm(keycloakConfig.realm());
    }

    private UserRepresentation getSuperAdminUserRepresentation() {
        RoleRepresentation role = getOrCreateRole(this.keycloakConfig.realm(), "super_admin");

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue(superAdminConfig.password());
        credentialRepresentation.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(superAdminConfig.username());
        user.setFirstName(superAdminConfig.firstName());
        user.setLastName(superAdminConfig.lastName());
        user.setEmail(superAdminConfig.email());
        user.setEnabled(true);
        user.setEmailVerified(true);

        user.setCredentials(List.of(credentialRepresentation));
        return user;
    }

    private RoleRepresentation getOrCreateRole(String realm, String roleName) {
        RolesResource rolesResource = this.keycloak.realm(realm).roles();

        try {
            return rolesResource.get(roleName).toRepresentation();
        } catch (ClientWebApplicationException e) {
            System.out.println("Failed to retrieve role: " + e.getMessage());
            RoleRepresentation roleRepresentation = new RoleRepresentation(roleName, roleName, false);
            rolesResource.create(roleRepresentation);

            return rolesResource.get(roleName).toRepresentation();
        }
    }
}

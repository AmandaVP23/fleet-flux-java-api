package dev.amanda.oidc;

import dev.amanda.config.SuperAdminConfig;
import dev.amanda.organization.exceptions.RealmAlreadyExistsException;
import dev.amanda.user.domain.Roles;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.*;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class KeycloakAdmin {
    private Keycloak keycloak;

    @Inject
    KeycloakConfig keycloakConfig;

    @Inject
    KeycloakSMTPServerConfig keycloakSMTPServerConfig;

    @Inject
    SuperAdminConfig superAdminConfig;

    @PostConstruct
    void init() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(this.keycloakConfig.serverUrl())
                .realm(this.keycloakConfig.realm())
                .grantType(this.keycloakConfig.grantType())
                .clientId(this.keycloakConfig.clientId())
                .username(this.keycloakConfig.username())
                .password(this.keycloakConfig.password())
                .build();
    }

    @PreDestroy
    void destroy() {
        this.keycloak.close();
    }

    public RealmRepresentation createRealm(String realmName, String displayName) {
        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm(realmName);
        realm.setDisplayName(displayName);
        realm.setSmtpServer(Map.of(
                "host", keycloakSMTPServerConfig.host(),
                "port", String.valueOf(keycloakSMTPServerConfig.port()),
                "from", keycloakSMTPServerConfig.from(),
                "auth", keycloakSMTPServerConfig.auth(),
                "starttls", keycloakSMTPServerConfig.starttls()
        ));

        List<ClientRepresentation> clients = new ArrayList<>();

        ClientRepresentation webClient = new ClientRepresentation();
        webClient.setClientId("web");
        webClient.setRedirectUris(List.of("*"));
        webClient.setEnabled(true);

        clients.add(webClient);

        realm.setClients(clients);
        realm.setEnabled(true);

        try {
            this.keycloak.realms().create(realm);
        } catch (WebApplicationException e) {
            Response response = e.getResponse();
            if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                throw new RealmAlreadyExistsException(realmName);
            }
            throw new RuntimeException(e);
        }  catch (Exception e) {
            throw new RuntimeException("Failed to create realm", e);
        }

        grantAdminAccessToNewRealm(realmName);

        return this.keycloak.realm(realmName).toRepresentation();
    }

    private void grantAdminAccessToNewRealm(String realmName) {
        try {
            List<ClientRepresentation> clients = this.keycloak.realm("master")
                    .clients().findByClientId(realmName + "-realm");

            if (clients.isEmpty()) {
                throw new RuntimeException("Could not find master client for realm: " + realmName);
            }

            String realmClientUUID = clients.getFirst().getId();

            Set<String> desiredRoles = Set.of(
                    "manage-users",
                    "manage-clients",
                    "manage-realm",
                    "create-client",
                    "impersonation",
                    "query-clients",
                    "view-users",
                    "view-clients"
            );

            List<RoleRepresentation> rolesToAssign = this.keycloak.realm(this.keycloakConfig.realm())
                    .clients()
                    .get(realmClientUUID)
                    .roles()
                    .list()
                    .stream()
                    .filter(role -> desiredRoles.contains(role.getName()))
                    .collect(Collectors.toList());

            if (rolesToAssign.isEmpty()) {
                throw new RuntimeException("No matching roles found for realm client: " + realmName);
            }

            List<UserRepresentation> users = this.keycloak
                    .realm(this.keycloakConfig.realm())
                    .users()
                    .searchByUsername(this.keycloakConfig.username(), true);

            if (users.isEmpty()) {
                throw new RuntimeException("Could not find user: " + this.keycloakConfig.username());
            }

            String masterAdminUserId = users.getFirst().getId();

            this.keycloak.realm(this.keycloakConfig.realm())
                    .users()
                    .get(masterAdminUserId)
                    .roles()
                    .clientLevel(realmClientUUID)
                    .add(rolesToAssign);

            refreshKeycloakToken();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to grant admin access to realm " + realmName + ": " + e.getMessage());
        }
    }

    public UserRepresentation createRealmUser(String realm, String firstName, String lastName, String email, String roleName) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEnabled(true);
        user.setRequiredActions(List.of("UPDATE_PASSWORD"));
        user.setEmailVerified(false);

        try (Response response = this.keycloak.realm(realm).users().create(user)) {
            if (response.getStatus() != 201) {
                String body = response.readEntity(String.class);
                throw new RuntimeException("Failed to create user: " + response.getStatus() + " " + body);
            }
        }

        String createdUserId = null;

        try {
            UserRepresentation createdUser = getUser(realm, email)
                    .orElseThrow(() -> new IllegalArgumentException("User was created but could not be retrieved"));

            createdUserId = createdUser.getId();

            RoleRepresentation role = getOrCreateRole(realm, roleName);

            this.keycloak.realm(realm)
                    .users()
                    .get(createdUserId)
                    .roles()
                    .realmLevel()
                    .add(List.of(role));

            this.keycloak.realm(realm)
                    .users()
                    .get(createdUserId)
                    .executeActionsEmail(List.of("UPDATE_PASSWORD"));

            return createdUser;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (createdUserId != null) {
                deleteUser(realm, createdUserId);
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to set up user " + email, e);
        }
    }

    public UserRepresentation createSuperAdminUser() {
        UserRepresentation user = getSuperAdminUserRepresentation();
        RealmResource superAdminRealm = this.keycloak.realm(superAdminConfig.realm());
        String createdUserId = null;

        try (Response response = superAdminRealm.users().create(user)) {
            if (response.getStatus() != 201) {
                String body = response.readEntity(String.class);
                throw new RuntimeException("Failed to create user: " + response.getStatus() + " " + body);
            }
        }

        try {
            UserRepresentation createdUser = getUser(superAdminConfig.realm(), superAdminConfig.email())
                    .orElseThrow(() -> new IllegalArgumentException("User was created but could not be retrieved"));

            createdUserId = createdUser.getId();

            RoleRepresentation role = getOrCreateRole(superAdminConfig.realm(), Roles.SUPER_ADMIN);

            superAdminRealm
                    .users()
                    .get(createdUserId)
                    .roles()
                    .realmLevel()
                    .add(List.of(role));

            return createdUser;
        } catch (Exception e) {
            if (createdUserId != null) {
                deleteUser(superAdminConfig.realm(), createdUserId);
            }
            throw new RuntimeException("Failed to set up super admin user", e);
        }
    }

    public Optional<UserRepresentation> getUser(String realm, String userEmail) {
        List<UserRepresentation> userRepresentationList = this.keycloak
                .realm(realm)
                .users()
                .searchByEmail(userEmail,  true);

        return userRepresentationList.stream().findFirst();
    }

    public void deleteUser(String realm, String userKeycloakId) {
        try (Response response = this.keycloak.realm(realm).users().delete(userKeycloakId)) {
            if (response.getStatus() != 204) {
                throw new RuntimeException("Failed to delete user: " + response.getStatus());
            }
        }
    }

    public void deleteRealm(String realmName) {
        try {
            keycloak.realm(realmName).remove();
        } catch (Exception e) {
            // todo - print to logger
            e.printStackTrace();
        }
    }

    private UserRepresentation getSuperAdminUserRepresentation() {
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
            if (e.getResponse().getStatus() != 404) {
                throw new RuntimeException("Failed to retrieve role: " + roleName, e);
            }

            rolesResource.create(new RoleRepresentation(roleName, roleName, false));
            return rolesResource.get(roleName).toRepresentation();
        }
    }

    private void refreshKeycloakToken() {
        this.keycloak.tokenManager().refreshToken();
    }
}

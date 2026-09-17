package dev.amanda.oidc;

import dev.amanda.config.SuperAdminConfig;
import dev.amanda.organization.exceptions.RealmAlreadyExistsException;
import dev.amanda.user.domain.Roles;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.*;
import org.keycloak.representations.userprofile.config.UPAttribute;
import org.keycloak.representations.userprofile.config.UPAttributePermissions;
import org.keycloak.representations.userprofile.config.UPConfig;

import java.util.*;
import java.util.stream.Collectors;

@Log
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
//        this.keycloak = KeycloakBuilder.builder()
//                .serverUrl(this.keycloakConfig.serverUrl())
//                .realm(this.keycloakConfig.realm())
//                .grantType(this.keycloakConfig.grantType())
//                .clientId(this.keycloakConfig.clientId())
//                .username(this.keycloakConfig.username())
//                .password(this.keycloakConfig.password())
//                .build();
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(this.keycloakConfig.serverUrl())
                .realm(this.keycloakConfig.realm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(this.keycloakConfig.clientId())
                .clientSecret("T3OW47ivZ4Ym7yIuf7B2orEvAmMvSvLB")
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

        ProtocolMapperRepresentation mapper = getProtocolMapperRepresentation();

        List<ClientRepresentation> clients = new ArrayList<>();

        ClientRepresentation webClient = new ClientRepresentation();
        webClient.setClientId("web"); // todo - put this in application properties
        webClient.setRedirectUris(List.of("*"));
        webClient.setEnabled(true);
        webClient.setPublicClient(true);
        // useful for typescript tester
        webClient.setDirectAccessGrantsEnabled(true);

        clients.add(webClient);

        realm.setClients(clients);
        realm.setEnabled(true);

        try {
            this.keycloak.realms().create(realm);
        } catch (WebApplicationException e) {
            log.severe(e.getMessage());
            Response response = e.getResponse();
            if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                throw new RealmAlreadyExistsException(realmName);
            }
            throw new RuntimeException(e);
        }  catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException("Failed to create realm", e);
        }

        grantAdminAccessToNewRealm(realmName);

        try {
            UPConfig upConfig = this.keycloak.realm(realmName)
                    .users().userProfile().getConfiguration();

            UPAttribute orgId = new UPAttribute();
            orgId.setName("organization_id");
            orgId.setDisplayName("Organization ID");

            UPAttributePermissions perms = new UPAttributePermissions();
            // Both view and edit must explicitly include "admin"
            perms.setView(Set.of("admin", "user"));
            perms.setEdit(Set.of("admin")); // only admins write it
            orgId.setPermissions(perms);

            upConfig.getAttributes().add(orgId);
            this.keycloak.realm(realmName).users().userProfile().update(upConfig);

            addOrganizationIdMapper(realmName);
        } catch (Exception e) {
            log.severe(e.getMessage());
        }

        return this.keycloak.realm(realmName).toRepresentation();
    }

    public Optional<RealmRepresentation> getRealm(String realmName) {
        log.info("Getting keycloak realm: " + realmName);
        try {
            RealmRepresentation realmRepresentation = this.keycloak
                    .realm(realmName)
                    .toRepresentation();

            return Optional.of(realmRepresentation);
        } catch (ClientWebApplicationException e) {
            return Optional.empty();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    private void addOrganizationIdMapper(String realmName) {
        ClientRepresentation client = keycloak.realm(realmName)
                .clients().findByClientId("web").get(0);

        var protocolMappersResource = keycloak.realm(realmName)
                .clients().get(client.getId())
                .getProtocolMappers();

        boolean exists = protocolMappersResource.getMappers().stream()
                .anyMatch(m -> "organization_id".equals(m.getName()));

        if (!exists) {
            try (Response response = protocolMappersResource
                    .createMapper(getProtocolMapperRepresentation())) {
                if (response.getStatus() != 201) {
                    String body = response.readEntity(String.class);
                    throw new RuntimeException("Failed to create mapper: " + body);
                }
            }
        }
    }

    private static ProtocolMapperRepresentation getProtocolMapperRepresentation() {
        ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
        mapper.setName("organization_id");
        mapper.setProtocol("openid-connect");
        mapper.setProtocolMapper("oidc-usermodel-attribute-mapper");

        Map<String, String> config = new HashMap<>();
        config.put("user.attribute", "organization_id");
        config.put("claim.name", "organization_id");
        config.put("jsonType.label", "String");
        config.put("access.token.claim", "true");
        config.put("id.token.claim", "true");
        config.put("userinfo.token.claim", "true");  // was missing
        config.put("multivalued",          "false");

        mapper.setConfig(config);
        return mapper;
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
            log.severe("Failed to create user: " + createdUserId + " " + e.getMessage());
            if (createdUserId != null) {
                deleteUser(realm, createdUserId);
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to set up user " + email, e);
        }
    }

    public void setUserOrganizationId(String realm, String userId, long organizationId) {
        UserRepresentation userRepresentation = keycloak.realm(realm).users().get(userId).toRepresentation();

        // Get existing attributes or create a new mutable map
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }

        // Merge — don't replace
        attributes.put("organization_id", List.of(String.valueOf(organizationId)));
        userRepresentation.setAttributes(attributes);

        try {
            keycloak.realm(realm).users().get(userId).update(userRepresentation);
        } catch (WebApplicationException e) {
            String body = e.getResponse().readEntity(String.class);
            log.severe("Keycloak user update failed: " + body);
            log.severe(e.getMessage());
            // todo - throw
            throw e;
        }
    }

    public UserRepresentation createSuperAdminUser() {
        UserRepresentation user = getSuperAdminUserRepresentation();
        RealmResource superAdminRealm = this.keycloak.realm(superAdminConfig.realm());
        String createdUserId = null;

        try (Response response = superAdminRealm.users().create(user)) {
            if (response.getStatus() != 201) {
                String body = response.readEntity(String.class);
                log.severe("Failed to create user: " + response.getStatus() + " " + body);
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
            log.severe("Failed to set superadmin user \n" + e);
            throw new RuntimeException("Failed to set up super admin user", e);
        }
    }

    public Optional<UserRepresentation> getUser(String realm, String userEmail) {
        log.info("Getting keycloak user: " + userEmail);
        try {
            List<UserRepresentation> userRepresentationList = this.keycloak
                    .realm(realm)
                    .users()
                    .searchByEmail(userEmail,  true);

            return userRepresentationList.stream().findFirst();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public void deleteUser(String realm, String userKeycloakId) {
        try (Response response = this.keycloak.realm(realm).users().delete(userKeycloakId)) {
            if (response.getStatus() != 204) {
                throw new RuntimeException("KeycloakAdmin - Failed to delete user: " + response.getStatus());
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

    public void revokeAllSessions(String realm) {
        keycloak.realm(realm).logoutAll();
    }

    public void changeUserEnableState(String realm, String userId, boolean enable) {
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        user.setEnabled(enable);

        keycloak.realm(realm)
                .users()
                .get(userId)
                .update(user);
    }

    public void changeRealmUsersEnableState(String realm, boolean isEnable) {
        List<UserRepresentation> users = keycloak.realm(realm).users().list();

        for (UserRepresentation user : users) {
            user.setEnabled(isEnable);

            keycloak.realm(realm)
                    .users()
                    .get(user.getId())
                    .update(user);
        }
    }

    public void changeRealmClientsEnableState(String realm, boolean isEnable) {
        List<ClientRepresentation> clients = keycloak.realm(realm).clients().findAll();

        for (ClientRepresentation client : clients) {
            client.setEnabled(isEnable);

            keycloak.realm(realm)
                    .clients()
                    .get(client.getId())
                    .update(client);
        }
    }

    public void deleteAllRealmUsers(String realm) {
        List<UserRepresentation> users = keycloak.realm(realm).users().list();

        for (UserRepresentation user : users) {
            // todo - try/catch
            keycloak.realm(realm).users().delete(user.getId());
        }
    }

    public void deleteAllRealmClients(String realm) {
        List<ClientRepresentation> clients = keycloak.realm(realm).clients().findAll();

        for (ClientRepresentation client : clients) {
            keycloak.realm(realm)
                    .clients()
                    .delete(client.getId());
        }
    }

    public void changeRealmEnableState(String realm, boolean isEnable) {
        RealmRepresentation realmRepresentation = keycloak.realm(realm).toRepresentation();

        realmRepresentation.setEnabled(isEnable);

        keycloak.realm(realm).update(realmRepresentation);
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

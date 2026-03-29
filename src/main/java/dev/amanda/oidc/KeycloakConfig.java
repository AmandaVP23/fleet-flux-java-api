package dev.amanda.oidc;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "quarkus.keycloak.admin-client")
public interface KeycloakConfig {
    String serverUrl();

    String realm();

    String clientId();

    String grantType();

    String username();

    String password();
}

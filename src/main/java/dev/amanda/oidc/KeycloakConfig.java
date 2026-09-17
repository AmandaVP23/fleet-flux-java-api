package dev.amanda.oidc;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "quarkus.keycloak.admin-client")
public interface KeycloakConfig {
    String serverUrl();

    String realm();

    String clientId();

    String grantType();

    // todo - remove
    String username();

    String password();
}

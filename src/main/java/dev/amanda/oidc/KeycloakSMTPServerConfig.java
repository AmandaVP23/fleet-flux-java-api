package dev.amanda.oidc;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "quarkus.keycloak.smpt-server")
public interface KeycloakSMTPServerConfig {
    String host();
    int port();
    String from();
    String auth();
    String starttls();
}

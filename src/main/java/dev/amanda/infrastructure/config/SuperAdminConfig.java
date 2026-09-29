package dev.amanda.infrastructure.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "quarkus.super-admin")
public interface SuperAdminConfig {
    String username();
    String password();
    String firstName();
    String lastName();
    String email();
    String realm();
    String hostname();
}

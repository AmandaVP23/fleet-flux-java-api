package dev.amanda.oidc;

import dev.amanda.user.domain.Roles;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
public class AuthContextProvider {

    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken jwt;

    public AuthContext get() {
        AuthContext ctx = new AuthContext();

        if (hasRole(Roles.SUPER_ADMIN)) {
            ctx.role =  Roles.SUPER_ADMIN;
        } else if (hasRole(Roles.ORG_ADMIN)) {
            ctx.role =  Roles.ORG_ADMIN;
        }

        ctx.userKeycloakId = getUserKeycloakId();
        ctx.organizationId = this.getOrganizationId();

        return ctx;
    }

    public Long getOrganizationId() {
        String value = jwt.getClaim("organization_id");
        return value != null ? Long.valueOf(value) : null;
    }

    public boolean hasRole(String role) {
        return identity.hasRole(role);
    }

    public String getUserKeycloakId() {
        return jwt.getSubject();
    }

    public String getEmail() {
        return jwt.getClaim("email");
    }

    public String getFirstName() {
        return jwt.getClaim("given_name");
    }

    public String getLastName() {
        return jwt.getClaim("family_name");
    }

    public boolean isSuperAdmin() {
        return hasRole(Roles.SUPER_ADMIN);
    }
}

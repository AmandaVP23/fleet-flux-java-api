package dev.amanda.infrastructure.oidc;

import dev.amanda.user.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthContext {
    String userKeycloakId;
    String role;
    Long organizationId;

    public boolean isSuperAdmin() {
        return role != null && role.equalsIgnoreCase(Role.SUPER_ADMIN.toString());
    }
}

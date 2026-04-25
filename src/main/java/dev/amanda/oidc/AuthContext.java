package dev.amanda.oidc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthContext {
    String userId;
    String role;
    long organizationId;
}

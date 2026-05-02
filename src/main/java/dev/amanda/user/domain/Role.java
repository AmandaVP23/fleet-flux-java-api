package dev.amanda.user.domain;

import lombok.Getter;

@Getter
public enum Role {
    SUPER_ADMIN(Roles.SUPER_ADMIN),
    ORG_ADMIN(Roles.ORG_ADMIN),
    DRIVER(Roles.DRIVER),
    FLEET_MANAGER(Roles.FLEET_MANAGER), // operational control of vehicles/assigns drivers to vehicles/routes
    ANALYST(Roles.ANALYST);

    private final String value;

    Role(String value) {
        this.value = value;
    }
}

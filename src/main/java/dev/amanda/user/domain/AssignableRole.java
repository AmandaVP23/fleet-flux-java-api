package dev.amanda.user.domain;

import lombok.Getter;

@Getter
public enum AssignableRole {
    ORG_ADMIN(Role.ORG_ADMIN),
    DRIVER(Role.DRIVER),
    FLEET_MANAGER(Role.FLEET_MANAGER), // operational control of vehicles/assigns drivers to vehicles/routes
    ANALYST(Role.ANALYST);

    private final Role domainRole;

    AssignableRole(Role role) {
        this.domainRole = role;
    }

    public Role toRole() {
        return domainRole;
    }
}

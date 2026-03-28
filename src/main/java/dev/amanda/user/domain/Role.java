package dev.amanda.user.domain;

public enum Role {
    SUPER_ADMIN(Roles.SUPER_ADMIN),
    ORG_ADMIN(Roles.ORG_ADMIN),
    USER(Roles.USER);

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

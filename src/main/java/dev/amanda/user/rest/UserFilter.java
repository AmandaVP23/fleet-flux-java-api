package dev.amanda.user.rest;

import dev.amanda.user.domain.Role;

public record UserFilter (Long organizationId, Role role) {
    public UserFilter withOrganizationId(Long organizationId) {
        return new UserFilter(organizationId, role);
    }
}

package dev.amanda.infrastructure.shared.application;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.shared.exception.NotAllowedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class OrganizationAccessService {
    public Long getOrganizationId(AuthContext authContext, Long requestedOrgId) {
        if (authContext.isSuperAdmin()) {
            return requestedOrgId;
        }

        Long organizationId = authContext.getOrganizationId();

        if (organizationId == null) {
            throw new BadRequestException("Organization id is null");
        }

        if (requestedOrgId != null && !requestedOrgId.equals(organizationId)) {
            throw new NotAllowedException();
        }

        return organizationId;
    }
}

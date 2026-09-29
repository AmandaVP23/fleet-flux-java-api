package dev.amanda.infrastructure.shared.rest.organization_id_with_auth;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.oidc.AuthContextProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@ApplicationScoped
public class OrganizationIdWithAuthValidator implements ConstraintValidator<ValidOrganizationIdWithAuth, HasOrganizationId> {

    @Inject
    AuthContextProvider authContextProvider;

    @Override
    public boolean isValid(HasOrganizationId dto, ConstraintValidatorContext context) {
        AuthContext authContext = authContextProvider.get();

        if (authContext.isSuperAdmin() && dto.organizationId() == null) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("organizationId is required")
                    .addPropertyNode("organizationId")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}

package dev.amanda.user.rest.validation;

import dev.amanda.oidc.AuthContext;
import dev.amanda.oidc.AuthContextProvider;
import dev.amanda.user.dto.CreateUserRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@ApplicationScoped
public class CreateUserValidator implements ConstraintValidator<ValidCreateUser, CreateUserRequestDTO> {

    @Inject
    AuthContextProvider authContextProvider;

    @Override
    public boolean isValid(CreateUserRequestDTO dto, ConstraintValidatorContext context) {
        AuthContext authContext = authContextProvider.get();

        if (authContext.isSuperAdmin() && dto.organizationId == null) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("organizationId is required")
                    .addPropertyNode("organizationId")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}

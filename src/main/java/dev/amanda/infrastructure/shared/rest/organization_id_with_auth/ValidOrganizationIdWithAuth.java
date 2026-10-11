package dev.amanda.infrastructure.shared.rest.organization_id_with_auth;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrganizationIdWithAuthValidator.class)
public @interface ValidOrganizationIdWithAuth {
    String message() default "Invalid create user request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

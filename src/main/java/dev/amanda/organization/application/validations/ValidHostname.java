package dev.amanda.organization.application.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidHostnameValidator.class)
@Documented
public @interface ValidHostname {
    String message() default "Invalid hostname";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

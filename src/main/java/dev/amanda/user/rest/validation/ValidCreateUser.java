package dev.amanda.user.rest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreateUserValidator.class)
public @interface ValidCreateUser {
    String message() default "Invalid create user request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

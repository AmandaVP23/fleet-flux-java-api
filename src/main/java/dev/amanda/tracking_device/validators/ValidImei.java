package dev.amanda.tracking_device.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImeiValidator.class)
public @interface ValidImei {
    String message() default "Invalid IMEI";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

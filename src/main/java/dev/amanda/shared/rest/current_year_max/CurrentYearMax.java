package dev.amanda.shared.rest.current_year_max;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy =  CurrentYearMaxValidator.class)
@Documented
public @interface CurrentYearMax {

    String message() default "must not be greater than the current year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

//@Target({ ElementType.FIELD, ElementType.PARAMETER })
//@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = CurrentYearMaxValidator.class)
//@Documented


//String message() default "must not be greater than the current year";
//
//Class<?>[] groups() default {};
//
//Class<? extends Payload>[] payload() default {};
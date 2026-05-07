package dev.amanda.shared.rest.current_year_max;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class CurrentYearMaxValidator implements ConstraintValidator<CurrentYearMax, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // should use @NotNull if needed
        }

        return value <= Year.now().getValue();
    }
}

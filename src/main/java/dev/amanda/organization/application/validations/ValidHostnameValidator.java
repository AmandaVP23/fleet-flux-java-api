package dev.amanda.organization.application.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidHostnameValidator implements ConstraintValidator<ValidHostname, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (char c : value.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '-') {
                return false;
            }
        }

        return true;
    }
}

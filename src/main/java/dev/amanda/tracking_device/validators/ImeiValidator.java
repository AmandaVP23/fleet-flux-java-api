package dev.amanda.tracking_device.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImeiValidator implements ConstraintValidator<ValidImei, String> {
    @Override
    public boolean isValid(String imei, ConstraintValidatorContext ctx) {

        if (imei == null || imei.isEmpty()) return true; // @NotNull handles this
        if (!imei.matches("\\d{15}")) return false;
        int sum = 0;

        for (int i = 0; i < imei.length(); i++) {
            int digit = imei.charAt(i) - '0';

            if (i % 2 == 1) {
                digit *= 2;

                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
        }

        return sum % 10 == 0;
    }
}

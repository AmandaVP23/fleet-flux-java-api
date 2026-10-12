package dev.amanda.tracking_device.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImeiValidator implements ConstraintValidator<ValidImei, String> {
    @Override
    public boolean isValid(String imei, ConstraintValidatorContext ctx) {
        if (imei == null || imei.isEmpty()) return true; // @NotNull handles this
        if (imei.matches("\\d{15}")) return false;

        int sum = 0;
        for (int i = 0; i < 15; i++) {
            int d = imei.charAt(14 - i) - '0';
            if (i % 2 == 1) {
                d *= 2;
                if (d > 9) d -= 9;
            }

            sum += d;
        }

        return sum % 10 == 0;
    }
}

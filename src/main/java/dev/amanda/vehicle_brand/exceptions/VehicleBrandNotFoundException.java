package dev.amanda.vehicle_brand.exceptions;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;

public class VehicleBrandNotFoundException extends BaseApiException {
    public VehicleBrandNotFoundException() {
        super(ApiError.VEHICLE_BRAND_NOT_FOUND);
    }
}

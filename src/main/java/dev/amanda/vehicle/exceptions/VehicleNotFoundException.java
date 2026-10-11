package dev.amanda.vehicle.exceptions;

import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;

public class VehicleNotFoundException extends BaseApiException {
    public VehicleNotFoundException() {
        super(ApiError.VEHICLE_NOT_FOUND);
    }
}

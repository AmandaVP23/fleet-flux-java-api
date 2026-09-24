package dev.amanda.vehicle_driver_assignment.exceptions;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;

public class UserIsNotDriverException extends BaseApiException {
    public UserIsNotDriverException() {
        super(ApiError.USER_IS_NOT_DRIVER);
    }
}

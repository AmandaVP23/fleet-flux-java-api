package dev.amanda.user.exceptions;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;

public class UserNotFoundException extends BaseApiException {
    public UserNotFoundException() {
        super(ApiError.USER_NOT_FOUND);
    }
}

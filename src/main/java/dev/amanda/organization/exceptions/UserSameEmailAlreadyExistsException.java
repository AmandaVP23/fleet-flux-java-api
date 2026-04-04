package dev.amanda.organization.exceptions;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;

public class UserSameEmailAlreadyExistsException extends BaseApiException {
    public UserSameEmailAlreadyExistsException() {
        super(ApiError.USER_WITH_EMAIL_ALREADY_EXISTS);
    }
}

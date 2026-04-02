package dev.amanda.organization.exceptions;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;

public class OrganizationAlreadyExistsException extends BaseApiException {
    public OrganizationAlreadyExistsException() {
        super(ApiError.ORGANIZATION_WITH_SAME_NAME_ALREADY_EXISTS);
    }
}

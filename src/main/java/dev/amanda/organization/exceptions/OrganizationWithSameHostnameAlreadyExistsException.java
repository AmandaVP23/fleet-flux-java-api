package dev.amanda.organization.exceptions;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;

public class OrganizationWithSameHostnameAlreadyExistsException extends BaseApiException {
    public OrganizationWithSameHostnameAlreadyExistsException() {
        super(ApiError.ORGANIZATION_WITH_SAME_HOSTNAME_ALREADY_EXISTS);
    }
}

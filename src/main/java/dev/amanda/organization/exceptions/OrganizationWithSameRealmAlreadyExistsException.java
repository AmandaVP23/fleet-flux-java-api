package dev.amanda.organization.exceptions;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;

public class OrganizationWithSameRealmAlreadyExistsException extends BaseApiException {
    public OrganizationWithSameRealmAlreadyExistsException() {
        super(ApiError.ORGANIZATION_WITH_SAME_GENERATED_REALM_ALREADY_EXISTS);
    }
}

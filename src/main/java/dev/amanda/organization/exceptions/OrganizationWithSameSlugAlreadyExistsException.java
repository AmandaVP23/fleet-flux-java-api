package dev.amanda.organization.exceptions;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;

public class OrganizationWithSameSlugAlreadyExistsException extends BaseApiException {
    public OrganizationWithSameSlugAlreadyExistsException() {
        super(ApiError.ORGANIZATION_WITH_SAME_GENERATED_REALM_ALREADY_EXISTS);
    }
}

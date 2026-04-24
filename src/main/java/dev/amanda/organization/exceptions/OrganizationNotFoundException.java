package dev.amanda.organization.exceptions;

import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;

public class OrganizationNotFoundException extends BaseApiException  {
    public OrganizationNotFoundException() {
        super(ApiError.ORGANIZATION_NOT_FOUND);
    }
}

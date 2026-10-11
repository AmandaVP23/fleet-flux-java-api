package dev.amanda.organization.exceptions;

import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;

public class RealmAlreadyExistsException extends BaseApiException {
    public RealmAlreadyExistsException(String realmName) {
        super(ApiError.REALM_KEYCLOAK_CONFLICT, new Object[]{realmName});
    }
}

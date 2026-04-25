package dev.amanda.organization.application;

public class RestoreSoftDeletedOrganization {
    public void execute() {
        // TODO [DB] Load organization by ID
        // TODO [VALIDATION] Ensure organization is INACTIVE

        // TODO [DB] Set organization.status = ACTIVE
        // TODO [DB] Clear deletedAt
        // TODO [DB] Persist changes

        // TODO [TENANT] Resolve realm name

        // --- KEYCLOAK OPERATIONS (reverse order) ---

        // TODO [KEYCLOAK] Fetch all clients in realm
        // TODO [KEYCLOAK] For each client:
        //   - set enabled = true

        // TODO [KEYCLOAK] Fetch all users in realm
        // TODO [KEYCLOAK] For each user:
        //   - set enabled = true

        // TODO [KEYCLOAK][OPTIONAL] Remove realm attribute:
        //   attributes.status = "ACTIVE"

        // TODO [CACHE] Warm or reset tenant caches

        // TODO [LOGGING] Audit log:
        //   - action = RESTORE
    }
}

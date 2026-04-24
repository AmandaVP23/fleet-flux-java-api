package dev.amanda.organization.application;

public class HardDeleteOrganizationUseCase {
    // TODO [DB] Ensure organization is already INACTIVE
    // TODO [VALIDATION] Ensure retention period has passed

    // TODO [TENANT] Resolve realm name

    // TODO [KEYCLOAK] Export realm (backup)
    // TODO [KEYCLOAK] DELETE /admin/realms/{realm}

    // TODO [DB] Anonymize or delete related data (GDPR compliance)

    // TODO [DB] Remove organization record

    // TODO [LOGGING] Audit log:
    //   - action = HARD_DELETE

    public void execute() {}
}

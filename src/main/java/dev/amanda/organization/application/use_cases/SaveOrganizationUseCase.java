package dev.amanda.organization.application.use_cases;

import dev.amanda.organization.application.mappers.OrganizationMapper;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.persistence.OrganizationRepositoryPanache;
import dev.amanda.organization.dto.CreateOrganizationDTO;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.user.domain.Role;
import dev.amanda.user.domain.User;
import dev.amanda.user.persistence.UserRepositoryPersistence;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SaveOrganizationUseCase {
    @Inject
    OrganizationRepositoryPanache organizationRepositoryPanache;

    @Inject
    UserRepositoryPersistence userRepositoryPersistence;

    @Inject
    OrganizationMapper organizationMapper;

    @Transactional
    public OrganizationResponseDTO execute(CreateOrganizationDTO dto, String realm, String userKeycloakId) {
        Organization organization = new Organization();
        organization.setName(dto.name);
        organization.setRealm(realm);
        organization.setHostname(dto.hostname);

        User user = new User();
        user.setKeycloakId(userKeycloakId);
        user.setEmail(dto.adminEmail);
        user.setOrganization(organization);
        user.setFirstName(dto.adminFirstName);
        user.setLastName(dto.adminLastName);
        user.setRole(Role.SUPER_ADMIN);

        organizationRepositoryPanache.persist(organization);
        userRepositoryPersistence.persist(user);

        return organizationMapper.toDto(organization);
    }
}

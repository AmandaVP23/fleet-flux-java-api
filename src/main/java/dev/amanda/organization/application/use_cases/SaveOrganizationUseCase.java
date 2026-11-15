package dev.amanda.organization.application.use_cases;

import dev.amanda.organization.application.mappers.OrganizationMapper;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.CreateOrganizationDTO;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.user.domain.Role;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SaveOrganizationUseCase {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationMapper organizationMapper;

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

        organizationRepository.persist(organization);
        userRepository.persist(user);

        return organizationMapper.toDto(organization);
    }
}

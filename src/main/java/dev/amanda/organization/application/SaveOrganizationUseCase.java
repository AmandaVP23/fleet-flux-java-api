package dev.amanda.organization.application;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.CreateOrganizationDTO;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SaveOrganizationUseCase {
    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public OrganizationResponseDTO execute(CreateOrganizationDTO dto, String realm, String userKeycloakId) {
        Organization organization = new Organization();
        organization.setName(dto.name);
        organization.setRealm(realm);

        User user = new User();
        user.setKeycloakId(userKeycloakId);
        user.setEmail(dto.adminEmail);
        user.setOrganization(organization);

        organizationRepository.persist(organization);
        userRepository.persist(user);

        return new OrganizationResponseDTO(
                organization.getId(),
                organization.getName()
        );
    }
}

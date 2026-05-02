package dev.amanda.user.application;

import dev.amanda.oidc.AuthContext;
import dev.amanda.oidc.KeycloakAdmin;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.user.domain.Roles;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.user.dto.CreateUserRequestDTO;
import dev.amanda.user.dto.UserResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationScoped
public class CreateUserUseCase {

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public UserResponseDTO execute(CreateUserRequestDTO dto, AuthContext authContext) {
        Long effectiveOrgId;

        if (authContext.isSuperAdmin()) {
            effectiveOrgId = dto.organizationId;
        } else {
            effectiveOrgId = authContext.getOrganizationId();
        }

        Organization organization = organizationRepository.findByIdOrThrow(effectiveOrgId);

        String userKeycloakId = null;
        try {
            UserRepresentation userRepresentation = keycloakAdmin.createRealmUser(
                    organization.getRealm(), dto.firstName, dto.lastName, dto.email, Roles.USER);

            userKeycloakId = userRepresentation.getId();

            User user = new User();
            user.setEmail(dto.email);
            user.setOrganization(organization);
            user.setKeycloakId(userKeycloakId);
            user.setFirstName(dto.firstName);
            user.setLastName(dto.lastName);

            userRepository.persist(user);

            return UserResponseDTO.from(user);
        } catch (Exception e) {
            if (userKeycloakId != null) {
                keycloakAdmin.deleteUser(organization.getRealm(), userKeycloakId);
            }

            throw e;
        }
    }
}

package dev.amanda.user.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.oidc.KeycloakAdmin;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.persistence.OrganizationRepositoryPanache;
import dev.amanda.organization.exceptions.UserSameEmailAlreadyExistsException;
import dev.amanda.user.application.mappers.UserMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.persistence.UserRepositoryPersistence;
import dev.amanda.user.dto.CreateUserRequestDTO;
import dev.amanda.user.dto.UserResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Optional;

@ApplicationScoped
public class CreateUserUseCase {

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Inject
    OrganizationRepositoryPanache organizationRepositoryPanache;

    @Inject
    UserRepositoryPersistence userRepositoryPersistence;

    @Inject
    UserMapper userMapper;

    @Transactional
    public UserResponseDTO execute(CreateUserRequestDTO dto, AuthContext authContext) {
        Long effectiveOrgId;

        if (authContext.isSuperAdmin()) {
            effectiveOrgId = dto.organizationId();
        } else {
            effectiveOrgId = authContext.getOrganizationId();
        }

        Optional<User> existingUser = userRepositoryPersistence.findByEmail(dto.email());
        if (existingUser.isPresent()) {
            throw new UserSameEmailAlreadyExistsException();
        }

        Organization organization = organizationRepositoryPanache.findActiveByIdOrThrow(effectiveOrgId);

        String userKeycloakId = null;
        try {
            UserRepresentation userRepresentation = keycloakAdmin.createRealmUser(
                    organization.getRealm(), dto.firstName(), dto.lastName(), dto.email(), dto.role().toRole().getValue());

            userKeycloakId = userRepresentation.getId();

            User user = new User();
            user.setEmail(dto.email());
            user.setOrganization(organization);
            user.setKeycloakId(userKeycloakId);
            user.setFirstName(dto.firstName());
            user.setLastName(dto.lastName());
            user.setRole(dto.role().toRole());

            userRepositoryPersistence.persist(user);

            return userMapper.toDto(user);
        } catch (Exception e) {
            if (userKeycloakId != null) {
                keycloakAdmin.deleteUser(organization.getRealm(), userKeycloakId);
            }

            throw e;
        }
    }
}

package dev.amanda.user.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.user.application.mappers.UserMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.persistence.UserRepositoryPersistence;
import dev.amanda.user.dto.UserResponseDTO;
import dev.amanda.user.exceptions.UserNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetUserByIdUseCase {

    @Inject
    UserRepositoryPersistence userRepositoryPersistence;

    @Inject
    UserMapper userMapper;

    public UserResponseDTO execute(long id, AuthContext authContext) {
        User user = userRepositoryPersistence.findByIdOrThrow(id);
        long userOrgId = user.getOrganization().getId();

        if (!authContext.isSuperAdmin() && authContext.getOrganizationId() != null && userOrgId != authContext.getOrganizationId()) {
            throw new UserNotFoundException();
        }

        return userMapper.toDto(user);
    }
}

package dev.amanda.user.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.user.application.mappers.UserMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.persistence.UserRepositoryPersistence;
import dev.amanda.user.dto.UserResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetCurrentUserUseCase {

    @Inject
    UserRepositoryPersistence userRepositoryPersistence;

    @Inject
    UserMapper userMapper;

    public UserResponseDTO execute(AuthContext authContext) {
        User user = userRepositoryPersistence.findByKeycloakIdOrThrow(authContext.getUserKeycloakId());

        return userMapper.toDto(user);
    }
}

package dev.amanda.user.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.user.application.mappers.UserMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.persistence.UserRepositoryPanache;
import dev.amanda.user.dto.UserResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetCurrentUserUseCase {

    @Inject
    UserRepositoryPanache userRepositoryPanache;

    @Inject
    UserMapper userMapper;

    public UserResponseDTO execute(AuthContext authContext) {
        User user = userRepositoryPanache.findByKeycloakIdOrThrow(authContext.getUserKeycloakId());

        return userMapper.toDto(user);
    }
}

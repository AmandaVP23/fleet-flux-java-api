package dev.amanda.user.application.use_cases;

import dev.amanda.oidc.AuthContext;
import dev.amanda.user.application.mappers.UserMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.user.dto.UserResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetCurrentUserUseCase {

    @Inject
    private UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    public UserResponseDTO execute(AuthContext authContext) {
        User user = userRepository.findByKeycloakIdOrThrow(authContext.getUserKeycloakId());

        return userMapper.toDto(user);
    }
}

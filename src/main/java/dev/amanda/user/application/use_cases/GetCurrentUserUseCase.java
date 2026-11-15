package dev.amanda.user.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.user.application.mappers.UserMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.user.dto.UserResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GetCurrentUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO execute(AuthContext authContext) {
        User user = userRepository.findByKeycloakIdOrThrow(authContext.getUserKeycloakId());

        return userMapper.toDto(user);
    }
}

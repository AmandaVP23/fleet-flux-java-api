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
public class GetUserByIdUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO execute(long id, AuthContext authContext) {
        User user = authContext.isSuperAdmin()
            ? userRepository.findByIdOrThrow(id)
            : userRepository.findByIdInOrganizationOrThrow(id, authContext.getOrganizationId());

        return userMapper.toDto(user);
    }
}

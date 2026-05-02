package dev.amanda.user.application.use_cases;

import dev.amanda.oidc.AuthContext;
import dev.amanda.user.application.mappers.UserMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.user.dto.UserResponseDTO;
import dev.amanda.user.exceptions.UserNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetUserByIdUseCase {

    @Inject
    private UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    public UserResponseDTO execute(long id, AuthContext authContext) {
        User user = userRepository.findByIdOrThrow(id);
        long userOrgId = user.getOrganization().getId();

        if (!authContext.isSuperAdmin() && authContext.getOrganizationId() != null && userOrgId != authContext.getOrganizationId()) {
            throw new UserNotFoundException();
        }

        return userMapper.toDto(user);
    }
}

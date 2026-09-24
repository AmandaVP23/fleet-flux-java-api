package dev.amanda.user.application.use_cases;

import dev.amanda.oidc.AuthContext;
import dev.amanda.shared.PageResult;
import dev.amanda.shared.application.PageRequestHelper;
import dev.amanda.shared.exception.NotAllowedException;
import dev.amanda.user.application.mappers.UserMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.user.dto.UserResponseDTO;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.List;

@ApplicationScoped
public class ListUsersUseCase {
    @Inject
    UserRepository userRepository;

    @Inject
    PageRequestHelper pageRequestHelper;

    @Inject
    UserMapper userMapper;

    public PageResult<UserResponseDTO> execute(int pageNumber, int pageSize, String sortBy, String direction, Long requestOrganizationId, AuthContext authContext) {
        pageRequestHelper.validate(pageNumber, pageSize, sortBy, direction, null);

        Sort sort = pageRequestHelper.buildSort(sortBy, direction);

        long total;

        List<UserResponseDTO> data;
        List<User> users;

        if (authContext.isSuperAdmin()) {
            if (requestOrganizationId != null) {
                total = userRepository.countByOrganization(requestOrganizationId);
                users = userRepository.findPaginatedByOrganization(requestOrganizationId, pageNumber, pageSize, sort);
            } else {
                total = userRepository.countAll();
                users = userRepository.findAllPaginated(pageNumber, pageSize, sort);
            }
        } else {
            Long organizationId = authContext.getOrganizationId();

            if (requestOrganizationId != null && !requestOrganizationId.equals(organizationId)) {
                throw new NotAllowedException();
            }

            if (organizationId == null) {
                throw new BadRequestException("Organization id is null");
            }

            total = userRepository.countByOrganization(organizationId);
            users = userRepository.findPaginatedByOrganization(organizationId, pageNumber, pageSize, sort);
        }

        data = users.stream()
                .map(userMapper::toDto)
                .toList();

        return new PageResult<>(data, total, pageNumber, pageSize);
    }
}

package dev.amanda.user.application.use_cases;

import dev.amanda.oidc.AuthContext;
import dev.amanda.shared.PageResult;
import dev.amanda.shared.application.OrganizationAccessService;
import dev.amanda.shared.application.PageRequestHelper;
import dev.amanda.user.application.mappers.UserMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import dev.amanda.user.dto.UserResponseDTO;
import dev.amanda.user.rest.UserFilter;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListUsersUseCase {
    @Inject
    UserRepository userRepository;

    @Inject
    PageRequestHelper pageRequestHelper;

    @Inject
    UserMapper userMapper;

    @Inject
    OrganizationAccessService organizationAccessService;

    public PageResult<UserResponseDTO> execute(int pageNumber, int pageSize, String sortBy, String direction, UserFilter userFilter, AuthContext authContext) {
        pageRequestHelper.validate(pageNumber, pageSize, sortBy, direction, null);

        Sort sort = pageRequestHelper.buildSort(sortBy, direction);

        Long organizationId = organizationAccessService.getOrganizationId(authContext, userFilter.organizationId());
        userFilter = userFilter.withOrganizationId(organizationId);

        long total = userRepository.count(userFilter);

        List<User> users = userRepository.findPaginated(pageNumber, pageSize, sort, userFilter);
        List<UserResponseDTO> data = users.stream()
                .map(userMapper::toDto)
                .toList();

        return new PageResult<>(data, total, pageNumber, pageSize);
    }
}

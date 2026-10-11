package dev.amanda.user.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.shared.PageResult;
import dev.amanda.infrastructure.shared.application.OrganizationAccessService;
import dev.amanda.infrastructure.shared.application.PageRequestHelper;
import dev.amanda.user.application.mappers.UserMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.persistence.UserRepositoryPersistence;
import dev.amanda.user.dto.UserResponseDTO;
import dev.amanda.user.rest.UserFilter;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListUsersUseCase {
    @Inject
    UserRepositoryPersistence userRepositoryPersistence;

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

        long total = userRepositoryPersistence.count(userFilter);

        List<User> users = userRepositoryPersistence.findPaginated(pageNumber, pageSize, sort, userFilter);
        List<UserResponseDTO> data = users.stream()
                .map(userMapper::toDto)
                .toList();

        return new PageResult<>(data, total, pageNumber, pageSize);
    }
}

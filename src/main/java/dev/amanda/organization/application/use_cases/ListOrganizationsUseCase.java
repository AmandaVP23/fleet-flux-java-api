package dev.amanda.organization.application.use_cases;

import dev.amanda.organization.application.mappers.OrganizationMapper;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.OrganizationFilter;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.shared.PageResult;
import dev.amanda.shared.application.PageRequestHelper;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Set;

@ApplicationScoped
public class ListOrganizationsUseCase {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "name", "createdAt");

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    OrganizationMapper organizationMapper;

    @Inject
    PageRequestHelper pageRequestHelper;

    public PageResult<OrganizationResponseDTO> execute(int page, int size, String sortBy, String direction, OrganizationFilter filter) {
        pageRequestHelper.validate(page, size, sortBy, direction, ALLOWED_SORT_FIELDS);

        Sort sort = pageRequestHelper.buildSort(sortBy, direction);

        List<Organization> organizations = switch (filter) {
            case ACTIVE -> organizationRepository.findActivePaginated(page, size, sort);
            case DELETED -> organizationRepository.findDeletedPaginated(page, size, sort);
            case ALL -> organizationRepository.findPaginated(page, size, sort);
        };

        long total = switch (filter) {
            case ACTIVE -> organizationRepository.countActive();
            case DELETED -> organizationRepository.countDeleted();
            case ALL -> organizationRepository.count();
        };

        List<OrganizationResponseDTO> data = organizations.stream()
                .map(organizationMapper::toDto)
                .toList();

        return new PageResult<>(data, total, page, size);
    }
}

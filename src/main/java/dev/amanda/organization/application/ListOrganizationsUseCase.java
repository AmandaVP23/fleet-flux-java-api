package dev.amanda.organization.application;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.OrganizationFilter;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.shared.PageResult;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.List;
import java.util.Set;

@ApplicationScoped
public class ListOrganizationsUseCase {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "name", "createdAt");
    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    OrganizationRepository organizationRepository;

    public PageResult<OrganizationResponseDTO> execute(int page, int size, String sortBy, String direction, OrganizationFilter filter) {
        validate(page, size, sortBy, direction);

        Sort sort = buildSort(sortBy, direction);

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
                .map(OrganizationResponseDTO::from)
                .toList();

        return new PageResult<>(data, total, page, size);
    }

    private void validate(int page, int size, String sortBy, String direction) {
        if (page < 0) {
            throw new BadRequestException("'page' must be >= 0");
        }

        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new BadRequestException("'size' must be between 1 and " + MAX_PAGE_SIZE);
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new BadRequestException(
                    "'sortBy' must be one of: " + ALLOWED_SORT_FIELDS
            );
        }

        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new BadRequestException("'direction' must be 'asc' or 'desc'");
        }
    }

    private Sort buildSort(String sortBy, String direction) {
        return direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
    }
}

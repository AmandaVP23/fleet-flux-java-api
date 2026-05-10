package dev.amanda.vehicle.applications.use_cases;

import dev.amanda.oidc.AuthContext;
import dev.amanda.shared.PageResult;
import dev.amanda.shared.application.PageRequestHelper;
import dev.amanda.shared.exception.ApiError;
import dev.amanda.shared.exception.BaseApiException;
import dev.amanda.vehicle.applications.mappers.VehicleMapper;
import dev.amanda.vehicle.domain.VehicleRepository;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Set;

@ApplicationScoped
public class ListVehiclesUseCase {
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "createdAt");

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    PageRequestHelper pageRequestHelper;

    @Inject
    VehicleMapper vehicleMapper;

    public PageResult<VehicleResponseDTO> execute(int page, int size, String sortBy, String direction, Long organizationId, AuthContext authContext) {
        if (!authContext.isSuperAdmin() && organizationId != null) {
            throw new BaseApiException(ApiError.NOT_ALLOWED, "organizationId can only be specified by SUPER ADMIN");
        }

        pageRequestHelper.validate(page, size, sortBy, direction, ALLOWED_SORT_FIELDS);

        Sort sort = pageRequestHelper.buildSort(sortBy, direction);

        Long effectiveOrgId = authContext.isSuperAdmin() ? organizationId : authContext.getOrganizationId();
        long total = vehicleRepository.countWithQuery(effectiveOrgId);

        List<VehicleResponseDTO> data = vehicleRepository
                .listPaginated(effectiveOrgId, page, size, sort)
                .stream()
                .map(vehicleMapper::toDto)
                .toList();

        return new PageResult<>(data, total, page, size);
    }
}

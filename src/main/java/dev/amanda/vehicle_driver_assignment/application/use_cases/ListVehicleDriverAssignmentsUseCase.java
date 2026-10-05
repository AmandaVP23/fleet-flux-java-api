package dev.amanda.vehicle_driver_assignment.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.shared.PageResult;
import dev.amanda.infrastructure.shared.application.OrganizationAccessService;
import dev.amanda.infrastructure.shared.application.PageRequestHelper;
import dev.amanda.vehicle_driver_assignment.application.mappers.VehicleDriverAssignmentMapper;
import dev.amanda.vehicle_driver_assignment.domain.VehicleDriverAssignment;
import dev.amanda.vehicle_driver_assignment.persistence.VehicleDriverAssignmentRepositoryPanache;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentListResponseDTO;
import dev.amanda.vehicle_driver_assignment.rest.VehicleDriverAssignmentFilter;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListVehicleDriverAssignmentsUseCase {
    @Inject
    VehicleDriverAssignmentRepositoryPanache vehicleDriverAssignmentRepositoryPanache;

    @Inject
    VehicleDriverAssignmentMapper vehicleDriverAssignmentMapper;

    @Inject
    PageRequestHelper pageRequestHelper;

    @Inject
    OrganizationAccessService organizationAccessService;

    public PageResult<VehicleDriverAssignmentListResponseDTO> execute(int pageNumber, int pageSize, String sortBy, String direction, AuthContext authContext, VehicleDriverAssignmentFilter filter) {
        pageRequestHelper.validate(pageNumber, pageSize, sortBy, direction, null);

        Sort sort = pageRequestHelper.buildSort(sortBy, direction);

        Long organizationId = organizationAccessService.getOrganizationId(authContext, filter.organizationId());
        filter = filter.withOrganizationId(organizationId);

        long total = vehicleDriverAssignmentRepositoryPanache.count(filter);
        List<VehicleDriverAssignment> vehicleDriverAssignments = vehicleDriverAssignmentRepositoryPanache.findPaginated(pageNumber, pageSize, sort, filter);
        List<VehicleDriverAssignmentListResponseDTO> data = vehicleDriverAssignments.stream()
                .map(vehicleDriverAssignmentMapper::toDto)
                .toList();

        return new PageResult<>(data, total, pageNumber, pageSize);
    }
}

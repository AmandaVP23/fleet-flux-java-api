package dev.amanda.vehicle_driver_assignment.rest;

import dev.amanda.oidc.AuthContext;
import dev.amanda.oidc.AuthContextProvider;
import dev.amanda.shared.PageResult;
import dev.amanda.user.domain.Roles;
import dev.amanda.vehicle_driver_assignment.application.use_cases.CreateVehicleDriverAssignmentUseCase;
import dev.amanda.vehicle_driver_assignment.application.use_cases.ListVehicleDriverAssignmentsUseCase;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentListResponseDTO;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentRequestDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;

@Path("/vehicle-assignment")
public class VehicleDriverAssignmentResource {

    @Inject
    AuthContextProvider authProvider;

    @Inject
    CreateVehicleDriverAssignmentUseCase createVehicleDriverAssignmentUseCase;

    @Inject
    ListVehicleDriverAssignmentsUseCase listVehicleDriverAssignmentsUseCase;

    // todo - sortBy
    // todo - super admin have access to this?
    @GET
    @RolesAllowed({ Roles.SUPER_ADMIN, Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public PageResult<VehicleDriverAssignmentListResponseDTO> listVehicleDriverAssignments(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sortBy") @DefaultValue("id") String sortBy,
            @QueryParam("direction") @DefaultValue("asc") String direction,
            @QueryParam("organizationId") Long organizationId,
            @QueryParam("vehicleId") Long vehicleId,
            @QueryParam("driverId") Long driverId
    ) {
        AuthContext authContext = authProvider.get();
        VehicleDriverAssignmentFilter filter = new VehicleDriverAssignmentFilter(organizationId, driverId, vehicleId);

        return listVehicleDriverAssignmentsUseCase.execute(page, size, sortBy, direction, authContext, filter);
    }

    @POST
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public VehicleDriverAssignmentListResponseDTO createVehicleDriverAssignment(@Valid VehicleDriverAssignmentRequestDTO requestDTO) {
        AuthContext auth = authProvider.get();

        return createVehicleDriverAssignmentUseCase.execute(requestDTO, auth);
    }
}

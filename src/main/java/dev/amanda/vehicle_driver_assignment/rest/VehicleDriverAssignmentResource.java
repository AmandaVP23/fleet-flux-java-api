package dev.amanda.vehicle_driver_assignment.rest;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.oidc.AuthContextProvider;
import dev.amanda.infrastructure.shared.PageResult;
import dev.amanda.user.domain.Roles;
import dev.amanda.vehicle_driver_assignment.application.use_cases.CreateVehicleDriverAssignmentUseCase;
import dev.amanda.vehicle_driver_assignment.application.use_cases.EditVehicleDriverAssignmentEndDateUseCase;
import dev.amanda.vehicle_driver_assignment.application.use_cases.GetActiveVehicleDriverAssignmentUseCase;
import dev.amanda.vehicle_driver_assignment.application.use_cases.ListVehicleDriverAssignmentsUseCase;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentEndDateEditDTO;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentRequestDTO;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.time.Instant;

@Path("/assignments/drivers")
public class VehicleDriverAssignmentResource {

    @Inject
    AuthContextProvider authProvider;

    @Inject
    CreateVehicleDriverAssignmentUseCase createVehicleDriverAssignmentUseCase;

    @Inject
    EditVehicleDriverAssignmentEndDateUseCase editVehicleDriverAssignmentEndDateUseCase;

    @Inject
    ListVehicleDriverAssignmentsUseCase listVehicleDriverAssignmentsUseCase;

    @Inject
    GetActiveVehicleDriverAssignmentUseCase getActiveVehicleDriverAssignmentUseCase;

    // todo - sortBy
    @GET
    @RolesAllowed({ Roles.SUPER_ADMIN, Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public PageResult<VehicleDriverAssignmentResponseDTO> listVehicleDriverAssignments(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sortBy") @DefaultValue("id") String sortBy,
            @QueryParam("direction") @DefaultValue("asc") String direction,
            @QueryParam("organizationId") Long organizationId,
            @QueryParam("vehicleId") Long vehicleId,
            @QueryParam("driverId") Long driverId,
            @QueryParam("from") Instant from,
            @QueryParam("to") Instant to
            ) {
        AuthContext authContext = authProvider.get();
        VehicleDriverAssignmentFilter filter = new VehicleDriverAssignmentFilter(organizationId, driverId, vehicleId, from, to);

        return listVehicleDriverAssignmentsUseCase.execute(page, size, sortBy, direction, authContext, filter);
    }

    @POST
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public VehicleDriverAssignmentResponseDTO createVehicleDriverAssignment(@Valid VehicleDriverAssignmentRequestDTO requestDTO) {
        AuthContext auth = authProvider.get();

        return createVehicleDriverAssignmentUseCase.execute(requestDTO, auth);
    }

    @PATCH
    @Path("{id}")
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    @Operation(summary = "Edit vehicle - driver assignment end date")
    public VehicleDriverAssignmentResponseDTO editVehicleDriverAssignmentEndDate(@PathParam("id") Long id, @Valid VehicleDriverAssignmentEndDateEditDTO requestDTO) {
        AuthContext auth = authProvider.get();

        return editVehicleDriverAssignmentEndDateUseCase.execute(id, requestDTO, auth);
    }

    @GET
    @Path("active")
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    @Operation(summary = "Get an active assignment for a given time (if at not given defaults to now in UTC)")
    public VehicleDriverAssignmentResponseDTO getActiveVehicleDriverAssignments(
            @QueryParam("driverId") Long driverId,
            @QueryParam("vehicleId") Long vehicleId,
            @QueryParam("at") Instant at
            ) {
        AuthContext authContext = authProvider.get();

        return getActiveVehicleDriverAssignmentUseCase.execute(authContext, vehicleId, driverId, at);
    }
}

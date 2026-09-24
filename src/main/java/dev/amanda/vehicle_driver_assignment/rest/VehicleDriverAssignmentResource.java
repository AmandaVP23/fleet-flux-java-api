package dev.amanda.vehicle_driver_assignment.rest;

import dev.amanda.user.domain.Roles;
import dev.amanda.vehicle_driver_assignment.application.use_cases.CreateVehicleDriverAssignmentUseCase;
import dev.amanda.vehicle_driver_assignment.dto.VehicleDriverAssignmentRequestDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/vehicle-assignment")
public class VehicleDriverAssignmentResource {

    @Inject
    CreateVehicleDriverAssignmentUseCase createVehicleDriverAssignmentUseCase;

    // TODO - create endpoint for super admin to assign
    @POST
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public Response createVehicleDriverAssignment(@Valid VehicleDriverAssignmentRequestDTO requestDTO) {
        // TODO - VALIDATE DRIVER BELONGS IN SAME ORGANIZATION HAS THE LOGGED USER
        Map<String, String> response = new HashMap<>();
        response.put("message", "NOT IMPLEMENTED YET");

        createVehicleDriverAssignmentUseCase.execute(requestDTO);
        return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }
}

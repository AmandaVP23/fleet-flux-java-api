package dev.amanda.vehicle_device_assignment.rest;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.oidc.AuthContextProvider;
import dev.amanda.user.domain.Roles;
import dev.amanda.vehicle_device_assignment.dto.VehicleDeviceAssignmentDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/assignments/devices")
public class VehicleDeviceAssignmentResource {

    @Inject
    AuthContextProvider authProvider;

    @POST
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public String assignDeviceToVehicle(@Valid VehicleDeviceAssignmentDTO vehicleDeviceAssignmentDTO) {
        AuthContext auth = authProvider.get();

        return "hello";
    }
}

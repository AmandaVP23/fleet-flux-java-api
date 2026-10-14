package dev.amanda.tracking_device.rest;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.oidc.AuthContextProvider;
import dev.amanda.tracking_device.application.use_cases.CreateTrackingDeviceUseCase;
import dev.amanda.tracking_device.dto.CreateTrackingDeviceRequestDTO;
import dev.amanda.tracking_device.dto.TrackingDeviceResponseDTO;
import dev.amanda.user.domain.Roles;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/tracking-devices")
@Authenticated()
public class TrackingDeviceResource {

    @Inject
    AuthContextProvider authContextProvider;

    @Inject
    CreateTrackingDeviceUseCase createTrackingDeviceUseCase;


    // todo - create, edit, change status, remove

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public TrackingDeviceResponseDTO create(@Valid CreateTrackingDeviceRequestDTO dto) {
        AuthContext authContext = authContextProvider.get();

        return createTrackingDeviceUseCase.execute(dto, authContext);
    }
}

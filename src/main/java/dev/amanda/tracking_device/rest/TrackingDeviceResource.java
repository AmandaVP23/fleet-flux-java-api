package dev.amanda.tracking_device.rest;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.oidc.AuthContextProvider;
import dev.amanda.infrastructure.shared.PageResult;
import dev.amanda.tracking_device.application.use_cases.CreateTrackingDeviceUseCase;
import dev.amanda.tracking_device.application.use_cases.ListTrackingDevicesUseCase;
import dev.amanda.tracking_device.domain.TrackingDeviceStatus;
import dev.amanda.tracking_device.dto.ChangeTrackingDeviceStatusRequestDTO;
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

    @Inject
    ListTrackingDevicesUseCase listTrackingDevicesUseCase;


    // todo - create, edit, change status, remove

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public TrackingDeviceResponseDTO create(@Valid CreateTrackingDeviceRequestDTO dto) {
        AuthContext authContext = authContextProvider.get();

        return createTrackingDeviceUseCase.execute(dto, authContext);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public PageResult<TrackingDeviceResponseDTO> listTrackingDevices(
            @QueryParam("page") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sortBy") @DefaultValue("createdAt") String sortBy,
            @QueryParam("direction") @DefaultValue("asc") String direction
    ) {
        AuthContext authContext = authContextProvider.get();

        return listTrackingDevicesUseCase.execute(page, size, sortBy, direction, authContext.getOrganizationId());
    }

    @PATCH
    @Path("/{id}/status")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public void changeTrackingDeviceStatus(@PathParam("id") long id, ChangeTrackingDeviceStatusRequestDTO dto) {
        AuthContext authContext = authContextProvider.get();

    }

}

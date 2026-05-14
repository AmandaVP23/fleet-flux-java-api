package dev.amanda.vehicle.rest;

import dev.amanda.oidc.AuthContext;
import dev.amanda.oidc.AuthContextProvider;
import dev.amanda.shared.PageResult;
import dev.amanda.user.domain.Roles;
import dev.amanda.vehicle.applications.use_cases.CreateVehicleUseCase;
import dev.amanda.vehicle.applications.use_cases.EditVehicleUseCase;
import dev.amanda.vehicle.applications.use_cases.GetVehicleByIdUseCase;
import dev.amanda.vehicle.applications.use_cases.ListVehiclesUseCase;
import dev.amanda.vehicle.dto.CreateVehicleRequestDTO;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

@Path("/vehicles")
public class VehicleResource {

    @Inject
    AuthContextProvider authProvider;

    @Inject
    CreateVehicleUseCase createVehicleUseCase;

    @Inject
    ListVehiclesUseCase listVehiclesUseCase;

    @Inject
    GetVehicleByIdUseCase getVehicleByIdUseCase;

    @Inject
    EditVehicleUseCase editVehicleUseCase;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.SUPER_ADMIN, Roles.ORG_ADMIN })
    public VehicleResponseDTO create(@Valid CreateVehicleRequestDTO createVehicleRequestDTO) {
        AuthContext authContext = authProvider.get();

        return createVehicleUseCase.execute(createVehicleRequestDTO, authContext);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.SUPER_ADMIN, Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public PageResult<VehicleResponseDTO> listVehicles(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sortBy") @DefaultValue("createdAt") String sortBy,
            @QueryParam("direction") @DefaultValue("asc") String direction,
            @Parameter(
                    name = "organizationId",
                    description = "Only usable by SUPER_ADMIN."
            )
            @QueryParam("organizationId") Long organizationId,
            @QueryParam("brandId") Long brandId
    ) {
        AuthContext authContext = authProvider.get();
        VehicleFilter vehicleFilter = new VehicleFilter();
        vehicleFilter.setOrganizationId(organizationId);
        vehicleFilter.setBrandId(brandId);
        return listVehiclesUseCase.execute(page, size, sortBy, direction, vehicleFilter, authContext);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.SUPER_ADMIN, Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public VehicleResponseDTO findById(@PathParam("id") Long id) {
        AuthContext authContext = authProvider.get();
        return getVehicleByIdUseCase.execute(id, authContext);
    }

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.SUPER_ADMIN, Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public VehicleResponseDTO edit(@PathParam("id") Long id, @Valid CreateVehicleRequestDTO createVehicleRequestDTO) {
        AuthContext authContext = authProvider.get();
        return editVehicleUseCase.execute(id, createVehicleRequestDTO, authContext);
    }
}

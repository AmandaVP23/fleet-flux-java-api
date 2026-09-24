package dev.amanda.vehicle_brand.rest;

import dev.amanda.oidc.AuthContextProvider;
import dev.amanda.user.domain.Roles;
import dev.amanda.vehicle_brand.application.use_cases.CreateVehicleBrandUseCase;
import dev.amanda.vehicle_brand.application.use_cases.GetVehicleBrandByIdUseCase;
import dev.amanda.vehicle_brand.application.use_cases.ListVehicleBrandsUseCase;
import dev.amanda.vehicle_brand.application.use_cases.SoftDeleteVehicleBrandUseCase;
import dev.amanda.vehicle_brand.dto.CreateVehicleBrandRequestDTO;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.List;

@Path("/vehicle-brands")
public class VehicleBrandsResource {

    @Inject
    CreateVehicleBrandUseCase createVehicleBrandUseCase;

    @Inject
    ListVehicleBrandsUseCase listVehicleBrandsUseCase;

    @Inject
    GetVehicleBrandByIdUseCase getVehicleBrandByIdUseCase;

    @Inject
    SoftDeleteVehicleBrandUseCase softDeleteVehicleBrandUseCase;

    @Inject
    AuthContextProvider authProvider;

    @GET
    @Operation(
            summary = "Get all assignable vehicles brands"
    )
    @Authenticated
    public List<VehicleBrandResponseDTO> getAllActiveVehicleBrands(
            @QueryParam("filter") @DefaultValue("ONLY_ACTIVE") VehicleBrandDeletionFilter filter
    ) {
        return listVehicleBrandsUseCase.execute(filter, authProvider.get());
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Get assignable vehicles brand by id"
    )
    @Authenticated
    public VehicleBrandResponseDTO getVehicleBrandById(@PathParam("id") Long id) {
        return getVehicleBrandByIdUseCase.execute(id);
    }

    @POST
    @Operation(
            summary = "Create a new vehicle brand entry - Only SUPER ADMIN allowed"
    )
    @RolesAllowed(Roles.SUPER_ADMIN)
    public VehicleBrandResponseDTO createVehicleBrand(@Valid CreateVehicleBrandRequestDTO vehicleBrand) {
        return createVehicleBrandUseCase.execute(vehicleBrand);
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Get assignable vehicles brand by id (not deleted)"
    )
    @Authenticated
    public Response softDeleteVehicleBrand(@PathParam("id") Long id) {
        softDeleteVehicleBrandUseCase.execute(id);

        return Response.ok().build();
    }
}

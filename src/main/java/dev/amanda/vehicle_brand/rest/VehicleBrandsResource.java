package dev.amanda.vehicle_brand.rest;

import dev.amanda.user.domain.Roles;
import dev.amanda.vehicle_brand.application.use_cases.CreateVehicleBrandUseCase;
import dev.amanda.vehicle_brand.application.use_cases.ListVehicleBrandsUseCase;
import dev.amanda.vehicle_brand.dto.CreateVehicleBrandRequestDTO;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.HashMap;
import java.util.List;

@Path("/vehicle-brands")
public class VehicleBrandsResource {

    @Inject
    CreateVehicleBrandUseCase createVehicleBrandUseCase;

    @Inject
    ListVehicleBrandsUseCase listVehicleBrandsUseCase;

    @GET
    @Operation(
            summary = "Get all assignable vehicles brands (not deleted)"
    )
    @Authenticated
    public List<VehicleBrandResponseDTO> getAllActiveVehicleBrands() {
        return listVehicleBrandsUseCase.execute();
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Get assignable vehicles brand by id (not deleted)"
    )
    @Authenticated
    public Response getVehicleBrandById(@PathParam("id") Long id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "NOT IMPLEMENTED YET");

        return Response.ok(map).build();
    }

    @POST
    @Operation(
            summary = "Create a new vehicle brand entry"
    )
    @RolesAllowed(Roles.SUPER_ADMIN)
    public VehicleBrandResponseDTO createVehicleBrand(@Valid CreateVehicleBrandRequestDTO vehicleBrand) {
        return createVehicleBrandUseCase.execute(vehicleBrand);
    }
}

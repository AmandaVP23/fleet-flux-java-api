package dev.amanda.vehicle.rest;

import dev.amanda.oidc.AuthContext;
import dev.amanda.oidc.AuthContextProvider;
import dev.amanda.vehicle.applications.use_cases.CreateVehicleUseCase;
import dev.amanda.vehicle.dto.CreateVehicleRequestDTO;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/vehicle")
public class VehicleResource {

    @Inject
    AuthContextProvider authProvider;

    @Inject
    CreateVehicleUseCase createVehicleUseCase;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public VehicleResponseDTO create(@Valid CreateVehicleRequestDTO createVehicleRequestDTO) {
        AuthContext authContext = authProvider.get();

        return createVehicleUseCase.execute(createVehicleRequestDTO, authContext);
    }
}

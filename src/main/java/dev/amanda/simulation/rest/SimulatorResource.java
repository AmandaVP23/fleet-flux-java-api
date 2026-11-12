package dev.amanda.simulation.rest;

import dev.amanda.simulation.application.use_cases.ListSimulationVehiclesUseCase;
import dev.amanda.simulation.dto.SimulationDeviceResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/simulator")
public class SimulatorResource {

    @Inject
    ListSimulationVehiclesUseCase listSimulationVehiclesUseCase;

    @GET
    @RolesAllowed("simulator")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSimulatorData() {
        return "Hello World!";
    }

    @GET
    @Path("/devices")
    @RolesAllowed("simulator")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SimulationDeviceResponseDTO> getVehicleData() {
        return listSimulationVehiclesUseCase.execute();
    }
}

package dev.amanda.simulation.rest;

import dev.amanda.route.dto.RouteResponseDTO;
import dev.amanda.simulation.application.use_cases.GetSimulationRouteDetailsUseCase;
import dev.amanda.simulation.application.use_cases.ListSimulationRoutesUseCase;
import dev.amanda.simulation.application.use_cases.ListSimulationVehiclesUseCase;
import dev.amanda.simulation.dto.SimulationDeviceResponseDTO;
import dev.amanda.simulation.dto.SimulationRouteDetailsResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/simulator")
public class SimulatorResource {

    @Inject
    ListSimulationVehiclesUseCase listSimulationVehiclesUseCase;

    @Inject
    ListSimulationRoutesUseCase listSimulationRoutesUseCase;

    @Inject
    GetSimulationRouteDetailsUseCase getSimulationRouteDetailsUseCase;

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

    @GET
    @Path("/routes")
    @RolesAllowed("simulator")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RouteResponseDTO> getRoutes() {
        return listSimulationRoutesUseCase.execute();
    }

    @GET
    @Path("/routes/{id}/details")
    @RolesAllowed("simulator")
    @Produces(MediaType.APPLICATION_JSON)
    public SimulationRouteDetailsResponseDTO getRouteDetails(@PathParam("id") long id) {
        return getSimulationRouteDetailsUseCase.execute(id);
    }
}

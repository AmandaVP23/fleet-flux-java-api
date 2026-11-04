package dev.amanda.simulation.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/simulator")
public class SimulatorResource {

    @GET
    @RolesAllowed("simulator")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSimulatorData() {
        return "Hello World!";
    }
}

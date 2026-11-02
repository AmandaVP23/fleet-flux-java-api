package dev.amanda;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/simulator/ping")
public class SimulatorPingResource {
    @Inject
    SecurityIdentity identity;

    @GET
    @RolesAllowed("simulator")
    public String ping() {
        return "hello " + identity.getPrincipal().getName()
                + " roles=" + identity.getRoles();
    }
}

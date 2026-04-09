package dev.amanda;

import dev.amanda.user.domain.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @GET
    @Path("/auth")
    public String heyAuth() {
        return "Hello! You're authenticated";
    }

    @GET
    @Path("/auth-protected")
    @RolesAllowed(Roles.SUPER_ADMIN)
    public String heyAuthProtected() {
        return "Hello! You're authenticated and you're a super admin";
    }

    @GET
    @Path("/auth/org")
    @RolesAllowed(Roles.ORG_ADMIN)
    public String heyAuthOrgProtected() {
        return "Hey! You're authenticated and you're an org admin!!";
    }
}

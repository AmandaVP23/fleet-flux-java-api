package dev.amanda.user.rest;

import dev.amanda.oidc.AuthContext;
import dev.amanda.oidc.AuthContextProvider;
import dev.amanda.user.domain.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/users")
public class UsersResource {
    @Inject
    AuthContextProvider authProvider;

    @POST
    @RolesAllowed({Roles.SUPER_ADMIN, Roles.ORG_ADMIN})
    public Response createUser() {
        AuthContext auth = authProvider.get();
        System.out.println("Creating user: " + auth.getOrganizationId());

        Map<String, Object> obj = new HashMap<>();

        obj.put("message", "NOT IMPLEMENTED YET");
        return Response.status(400).entity(obj).build();
    }
}

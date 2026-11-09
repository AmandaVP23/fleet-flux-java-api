package dev.amanda.route.rest;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.oidc.AuthContextProvider;
import dev.amanda.route.application.use_cases.CreateRouteUseCase;
import dev.amanda.route.dto.CreateRouteRequestDTO;
import dev.amanda.route.dto.RouteResponseDTO;
import dev.amanda.user.domain.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/routes")
public class RouteResource {

    @Inject
    AuthContextProvider authProvider;

    @Inject
    CreateRouteUseCase createRouteUseCase;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.SUPER_ADMIN, Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public RouteResponseDTO create(@Valid CreateRouteRequestDTO createRouteRequestDTO) {
        AuthContext authContext = authProvider.get();

        return createRouteUseCase.execute(createRouteRequestDTO, authContext);
    }
}

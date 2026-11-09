package dev.amanda.route.rest;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.oidc.AuthContextProvider;
import dev.amanda.infrastructure.shared.domain.Coordinates;
import dev.amanda.route.application.use_cases.CreateRouteUseCase;
import dev.amanda.route.application.use_cases.GetRouteReviewUseCase;
import dev.amanda.route.domain.RoutePlan;
import dev.amanda.route.dto.CreateRouteRequestDTO;
import dev.amanda.route.dto.RouteResponseDTO;
import dev.amanda.user.domain.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/routes")
public class RouteResource {

    @Inject
    AuthContextProvider authProvider;

    @Inject
    CreateRouteUseCase createRouteUseCase;

    @Inject
    GetRouteReviewUseCase getRouteReviewUseCase;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public RouteResponseDTO create(@Valid CreateRouteRequestDTO createRouteRequestDTO) {
        AuthContext authContext = authProvider.get();

        return createRouteUseCase.execute(createRouteRequestDTO, authContext);
    }

    @GET
    @Path("/review")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ Roles.SUPER_ADMIN, Roles.ORG_ADMIN, Roles.FLEET_MANAGER })
    public RoutePlan getReview(@BeanParam @Valid RoutePreviewQuery query) {
        Coordinates origin = new Coordinates(query.getOriginLat(), query.getOriginLon());
        Coordinates destination = new Coordinates(query.getDestinationLat(), query.getDestinationLon());

        return getRouteReviewUseCase.execute(origin, destination);
    }

}

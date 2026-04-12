package dev.amanda.identity.rest;

import dev.amanda.identity.application.GetKeycloakConfigUseCase;
import dev.amanda.identity.dto.KeycloakConfigRequestDTO;
import dev.amanda.identity.dto.KeycloakConfigResponseDTO;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/keycloak-config")
public class KeycloakConfigResource {

    @Inject
    GetKeycloakConfigUseCase getKeycloakConfigUseCase;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Retrieve Keycloak configuration",
            description = "Returns Keycloak configuration based on the provided email."
    )
    @APIResponse(
            responseCode = "200",
            description = "Keycloak configuration successfully retrieved",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = KeycloakConfigResponseDTO.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid request payload"
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal server error"
    )
    public KeycloakConfigResponseDTO getKeycloakConfig(@Valid KeycloakConfigRequestDTO keycloakConfigRequestDTO) {
        return getKeycloakConfigUseCase.execute(keycloakConfigRequestDTO);
    }
}

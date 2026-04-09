package dev.amanda.identity.rest;

import dev.amanda.identity.application.GetKeycloakConfigUseCase;
import dev.amanda.identity.dto.KeycloakConfigRequestDTO;
import dev.amanda.identity.dto.KeycloakConfigResponseDTO;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/keycloak-config")
public class KeycloakConfigResource {

    @Inject
    GetKeycloakConfigUseCase getKeycloakConfigUseCase;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public KeycloakConfigResponseDTO getKeycloakConfig(@Valid KeycloakConfigRequestDTO keycloakConfigRequestDTO) {
        return getKeycloakConfigUseCase.execute(keycloakConfigRequestDTO);
    }
}

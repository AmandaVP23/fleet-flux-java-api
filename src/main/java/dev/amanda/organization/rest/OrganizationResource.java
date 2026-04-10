package dev.amanda.organization.rest;

import dev.amanda.organization.application.CreateOrganizationUseCase;
import dev.amanda.organization.dto.CreateOrganizationDTO;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.user.domain.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/organizations")
@RolesAllowed(Roles.SUPER_ADMIN)
public class OrganizationResource {

    @Inject
    CreateOrganizationUseCase createOrganizationUseCase;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Creates a new organization",
            description = "Allows a Super Admin user to create a new organization"
    )
    @APIResponse(
            responseCode = "200",
            description = "Organization successfully created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = OrganizationResponseDTO.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid request payload"
    )
    @APIResponse(
            responseCode = "409",
            description = "Conflict - org admin user or organization already exists"
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal server error"
    )
    public Response createOrganization(@Valid CreateOrganizationDTO createOrganizationDTO) {
        OrganizationResponseDTO orgResponseDTO = createOrganizationUseCase.execute(createOrganizationDTO);
        return Response.status(Response.Status.CREATED).entity(orgResponseDTO).build();
    }
}

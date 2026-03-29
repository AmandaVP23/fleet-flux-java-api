package dev.amanda.organization.api;

import dev.amanda.organization.application.CreateOrganizationUseCase;
import dev.amanda.organization.dto.CreateOrganizationDTO;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.user.domain.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/organizations")
@RolesAllowed(Roles.SUPER_ADMIN)
public class OrganizationResource {

    @Inject
    CreateOrganizationUseCase createOrganizationUseCase;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createOrganization(@Valid CreateOrganizationDTO createOrganizationDTO) {
        OrganizationResponseDTO orgResponseDTO = createOrganizationUseCase.execute(createOrganizationDTO);
        return Response.status(Response.Status.CREATED).entity(orgResponseDTO).build();
    }
}

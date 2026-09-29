package dev.amanda.user.rest;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.oidc.AuthContextProvider;
import dev.amanda.infrastructure.shared.PageResult;
import dev.amanda.user.application.use_cases.*;
import dev.amanda.user.domain.Role;
import dev.amanda.user.domain.Roles;
import dev.amanda.user.dto.CreateUserRequestDTO;
import dev.amanda.user.dto.UserResponseDTO;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

@Path("/users")
public class UsersResource {

    @Inject
    AuthContextProvider authProvider;

    @Inject
    CreateUserUseCase createUserUseCase;

    @Inject
    ListUsersUseCase listAllUsersUseCase;

    @Inject
    GetUserByIdUseCase getUserByIdUseCase;

    @Inject
    DeleteUserUseCase deleteUserUseCase;

    @Inject
    GetCurrentUserUseCase getCurrentUserUseCase;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.SUPER_ADMIN, Roles.ORG_ADMIN})
    @Operation(
            summary = "Creates a new user",
            description = "SuperAdmin should send the organization id, if user is OrgAdmin user will be created inside that user organization and organizationId should not be set"
    )
    public UserResponseDTO createUser(@Valid CreateUserRequestDTO dto) {
        AuthContext auth = authProvider.get();

        return createUserUseCase.execute(dto, auth);
    }

    // todo sortBy fullName -> firstName + lastName
    @GET
    @RolesAllowed({Roles.SUPER_ADMIN, Roles.ORG_ADMIN, Roles.FLEET_MANAGER})
    public PageResult<UserResponseDTO> listUsers(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sortBy") @DefaultValue("firstName") String sortBy,
            @QueryParam("direction") @DefaultValue("asc") String direction,
            @QueryParam("role") Role role,
            @QueryParam("organizationId") @Parameter(
                    description = "Can only be used by SUPER ADMIN"
            ) Long organizationId
    ) {
        AuthContext authContext = authProvider.get();

        UserFilter userFilter = new UserFilter(organizationId, role);

        return listAllUsersUseCase.execute(page, size, sortBy, direction, userFilter, authContext);
    }

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public UserResponseDTO getCurrentUser() {
        AuthContext authContext = authProvider.get();
        return getCurrentUserUseCase.execute(authContext);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.SUPER_ADMIN, Roles.ORG_ADMIN})
    public UserResponseDTO getUser(@PathParam("id") long id) {
        AuthContext authContext = authProvider.get();
        return getUserByIdUseCase.execute(id, authContext);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({Roles.SUPER_ADMIN, Roles.ORG_ADMIN})
    public void deleteUser(@PathParam("id") long id) {
        AuthContext authContext = authProvider.get();
        deleteUserUseCase.execute(id, authContext);
    }
}

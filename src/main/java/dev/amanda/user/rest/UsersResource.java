package dev.amanda.user.rest;

import dev.amanda.oidc.AuthContext;
import dev.amanda.oidc.AuthContextProvider;
import dev.amanda.shared.PageResult;
import dev.amanda.user.application.use_cases.CreateUserUseCase;
import dev.amanda.user.application.use_cases.DeleteUserUseCase;
import dev.amanda.user.application.use_cases.GetUserByIdUseCase;
import dev.amanda.user.application.use_cases.ListUsersUseCase;
import dev.amanda.user.domain.Roles;
import dev.amanda.user.dto.CreateUserRequestDTO;
import dev.amanda.user.dto.UserResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.SUPER_ADMIN, Roles.ORG_ADMIN})
    public UserResponseDTO createUser(@Valid CreateUserRequestDTO dto) {
        AuthContext auth = authProvider.get();

        return createUserUseCase.execute(dto, auth);
    }

    @GET
    @RolesAllowed({Roles.SUPER_ADMIN, Roles.ORG_ADMIN})
    public PageResult<UserResponseDTO> listAllUsersAsSuperAdmin(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sortBy") @DefaultValue("name") String sortBy,
            @QueryParam("direction") @DefaultValue("asc") String direction
    ) {
        AuthContext authContext = authProvider.get();
        return listAllUsersUseCase.execute(page, size, sortBy, direction, authContext);
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

package dev.amanda.user.rest;

import dev.amanda.oidc.AuthContext;
import dev.amanda.oidc.AuthContextProvider;
import dev.amanda.user.application.CreateUserUseCase;
import dev.amanda.user.domain.Roles;
import dev.amanda.user.dto.CreateUserRequestDTO;
import dev.amanda.user.dto.UserResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;


@Path("/users")
public class UsersResource {
    @Inject
    AuthContextProvider authProvider;

    @Inject
    CreateUserUseCase createUserUseCase;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.SUPER_ADMIN, Roles.ORG_ADMIN})
    public UserResponseDTO createUser(@Valid CreateUserRequestDTO dto) {
        AuthContext auth = authProvider.get();

        return createUserUseCase.execute(dto, auth);
    }
}

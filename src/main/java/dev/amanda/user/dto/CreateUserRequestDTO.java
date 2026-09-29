package dev.amanda.user.dto;

import dev.amanda.infrastructure.shared.rest.organization_id_with_auth.HasOrganizationId;
import dev.amanda.user.domain.AssignableRole;
import dev.amanda.infrastructure.shared.rest.organization_id_with_auth.ValidOrganizationIdWithAuth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ValidOrganizationIdWithAuth()
public record CreateUserRequestDTO (
    @NotBlank()
    @Length(min = 3, max = 80)
    String firstName,

    @NotBlank()
    @Length(min = 3, max = 80)
    String lastName,

    @NotBlank()
    @Length(min = 3, max = 80)
    @Email
    String email,

    Long organizationId,

    @NotNull()
    AssignableRole role
) implements HasOrganizationId {}

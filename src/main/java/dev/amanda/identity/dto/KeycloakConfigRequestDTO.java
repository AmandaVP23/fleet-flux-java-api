package dev.amanda.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class KeycloakConfigRequestDTO {
    @NotBlank()
    @Email
    @Schema(examples = "acme")
    public String slug;
}

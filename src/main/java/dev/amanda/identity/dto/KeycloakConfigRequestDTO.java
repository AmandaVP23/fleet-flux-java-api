package dev.amanda.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class KeycloakConfigRequestDTO {
    @NotBlank()
    @Email
    public String email;
}

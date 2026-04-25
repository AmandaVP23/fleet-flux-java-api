package dev.amanda.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class CreateUserRequestDTO {
    @NotBlank()
    @Length(min = 3, max = 80)
    public String name;

    @NotBlank()
    @Length(min = 3, max = 80)
    public String firstName;

    @NotBlank()
    @Length(min = 3, max = 80)
    public String lastName;

    @NotBlank()
    @Length(min = 3, max = 80)
    @Email
    public String admin;

    public long organizationId;
}

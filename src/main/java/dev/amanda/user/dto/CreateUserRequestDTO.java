package dev.amanda.user.dto;

import dev.amanda.user.rest.validation.ValidCreateUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@ValidCreateUser
public class CreateUserRequestDTO {
    @NotBlank()
    @Length(min = 3, max = 80)
    public String firstName;

    @NotBlank()
    @Length(min = 3, max = 80)
    public String lastName;

    @NotBlank()
    @Length(min = 3, max = 80)
    @Email
    public String email;

    public Long organizationId;
}

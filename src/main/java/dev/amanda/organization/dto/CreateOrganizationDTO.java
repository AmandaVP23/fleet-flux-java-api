package dev.amanda.organization.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class CreateOrganizationDTO {
    @NotBlank()
    @Length(min = 3, max = 80)
    public String name;

    @NotBlank()
    @Length(min = 3, max = 80)
    public String adminFirstName;

    @NotBlank()
    @Length(min = 3, max = 80)
    public String adminLastName;

    @NotBlank()
    @Length(min = 3, max = 80)
    public String adminEmail;
}

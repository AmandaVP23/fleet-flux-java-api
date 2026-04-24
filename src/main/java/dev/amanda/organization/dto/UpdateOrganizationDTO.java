package dev.amanda.organization.dto;

import org.hibernate.validator.constraints.Length;

public class UpdateOrganizationDTO {
    @Length(min = 3, max = 80)
    public String name;
}

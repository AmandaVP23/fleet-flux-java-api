package dev.amanda.organization.dto;

import dev.amanda.organization.domain.Organization;

public class OrganizationResponseDTO {
    public Long id;
    public String name;

    public static OrganizationResponseDTO from(Organization organization) {
        var dto = new OrganizationResponseDTO();
        dto.id = organization.getId();
        dto.name = organization.getName();
        return dto;
    }
}

package dev.amanda.organization.dto;

import dev.amanda.organization.domain.Organization;
import dev.amanda.shared.domain.BaseResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationResponseDTO extends BaseResponseDTO {
    private String name;

    public OrganizationResponseDTO(Organization org) {
        super(org);
        this.name = org.getName();
    }

    public  static OrganizationResponseDTO from(Organization org) {
        return new OrganizationResponseDTO(org);
    }
}
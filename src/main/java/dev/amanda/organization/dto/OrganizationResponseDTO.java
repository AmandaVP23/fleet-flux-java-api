package dev.amanda.organization.dto;

import dev.amanda.shared.application.BaseResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationResponseDTO extends BaseResponseDTO {
    private String name;

//    public OrganizationResponseDTO(Organization org) {
//        super(org);
//        this.name = org.getName();
//    }
//
//    public  static OrganizationResponseDTO from(Organization org) {
//        return new OrganizationResponseDTO(org);
//    }
}
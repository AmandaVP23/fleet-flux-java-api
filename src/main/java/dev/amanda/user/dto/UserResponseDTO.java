package dev.amanda.user.dto;

import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.shared.application.BaseResponseDTO;
import dev.amanda.user.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO extends BaseResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private OrganizationResponseDTO organization;
    private Role role;
}

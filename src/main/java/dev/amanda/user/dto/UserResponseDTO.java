package dev.amanda.user.dto;

import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.shared.application.BaseResponseDTO;
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

//    public UserResponseDTO(User user) {
//        super(user);
//        this.firstName = user.getFirstName();
//        this.lastName = user.getLastName();
//        this.email = user.getEmail();
//        this.organization = OrganizationResponseDTO.from(user.getOrganization());
//    }

//    public static UserResponseDTO from(User user) {
//        return new UserResponseDTO(user);
//    }
}

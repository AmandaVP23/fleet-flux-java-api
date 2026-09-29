package dev.amanda.organization.dto;

import dev.amanda.infrastructure.shared.application.BaseResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationResponseDTO extends BaseResponseDTO {
    private String name;
}
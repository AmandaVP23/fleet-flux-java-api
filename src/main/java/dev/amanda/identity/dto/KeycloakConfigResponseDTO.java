package dev.amanda.identity.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KeycloakConfigResponseDTO {
    private String serverUrl;
    private String clientId;
    private String realm;
}

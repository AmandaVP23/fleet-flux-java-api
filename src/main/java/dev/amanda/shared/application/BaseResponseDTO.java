package dev.amanda.shared.application;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class BaseResponseDTO {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
}
       
package dev.amanda.shared.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseResponseDTO {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;

    protected BaseResponseDTO(BaseEntity entity) {
        this.id = entity.getId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
       
package dev.amanda.organization.domain;

import dev.amanda.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Organization extends BaseEntity {

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false, unique = true)
    String realm;
}

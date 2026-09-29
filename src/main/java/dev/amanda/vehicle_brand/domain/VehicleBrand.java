package dev.amanda.vehicle_brand.domain;

import dev.amanda.infrastructure.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vehicle_brand")
@Getter
@Setter
public class VehicleBrand extends BaseEntity {
    @Column(nullable = false, unique = true)
    String name;
}

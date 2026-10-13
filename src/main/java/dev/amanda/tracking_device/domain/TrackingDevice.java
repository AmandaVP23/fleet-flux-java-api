package dev.amanda.tracking_device.domain;

import dev.amanda.organization.domain.Organization;
import dev.amanda.infrastructure.shared.domain.BaseEntity;
import dev.amanda.tracking_device.validators.ValidImei;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingDevice extends BaseEntity {

    @Column(length = 15)
    @ValidImei
    String imei;

    @Column()
    String model;

    @Column()
    @Enumerated(EnumType.STRING)
    TrackingDeviceStatus status;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    Organization organization;
}

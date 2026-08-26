package dev.amanda.tracking_device.domain;

import dev.amanda.organization.domain.Organization;
import dev.amanda.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingDevice extends BaseEntity {

    @Column(length = 20)
    String serialNumber;

    @Column()
    String model;

    @Column()
    @Enumerated(EnumType.STRING)
    TrackingDeviceStatus status;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    Organization organization;
}

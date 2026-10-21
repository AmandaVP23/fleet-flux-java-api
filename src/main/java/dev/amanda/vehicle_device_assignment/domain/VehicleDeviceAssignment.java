package dev.amanda.vehicle_device_assignment.domain;

import dev.amanda.infrastructure.shared.domain.BaseEntity;
import dev.amanda.tracking_device.domain.TrackingDevice;
import dev.amanda.vehicle.domain.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class VehicleDeviceAssignment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id")
    Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id")
    TrackingDevice device;

    @Column(name = "assigned_at")
    private Instant assignedAt;
}

package dev.amanda.vehicle_driver_assignment.domain;

import dev.amanda.shared.domain.BaseEntity;
import dev.amanda.user.domain.User;
import dev.amanda.vehicle.domain.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "vehicle_driver_assignment")
@Getter
@Setter
public class VehicleDriverAssignment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id")
    Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id")
    User driver;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date", updatable = false)
    private Instant endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_by")
    User assignedBy;
}

package dev.amanda.vehicle_driver_assignment.domain;

import dev.amanda.infrastructure.shared.domain.BaseEntity;
import dev.amanda.infrastructure.shared.exception.DomainError;
import dev.amanda.infrastructure.shared.exception.DomainException;
import dev.amanda.user.domain.User;
import dev.amanda.vehicle.domain.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

// todo - add notes

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

    @Column(name = "start_date_time", nullable = false)
    private Instant startDateTime;

    @Column(name = "end_date_time", updatable = false)
    private Instant endDateTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    public void setStartDateTime(Instant startDateTime) {
        if (startDateTime != null && this.endDateTime != null && this.endDateTime.isBefore(startDateTime)) {
            throw new DomainException(DomainError.VEHICLE_DRIVER_ASSIGNMENT_START_DATETIME_BEFORE_END_DATETIME);
        }

        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(Instant endDateTime) {
        if (endDateTime != null && this.startDateTime != null && this.endDateTime.isBefore(endDateTime)) {
            throw new DomainException(DomainError.VEHICLE_DRIVER_ASSIGNMENT_START_DATETIME_BEFORE_END_DATETIME);
        }

        this.endDateTime = endDateTime;
    }
}

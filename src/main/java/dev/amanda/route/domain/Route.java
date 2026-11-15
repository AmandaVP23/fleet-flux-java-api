package dev.amanda.route.domain;

import dev.amanda.infrastructure.shared.domain.BaseEntity;
import dev.amanda.infrastructure.shared.domain.Coordinates;
import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.vehicle.domain.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "route")
@Getter
@Setter
public class Route extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "scheduled_time", nullable = false)
    private Instant scheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RouteStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude",  column = @Column(name = "origin_lat",  nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "origin_lon",  nullable = false))
    })
    private Coordinates origin;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude",  column = @Column(name = "destination_lat",  nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "destination_lon",  nullable = false))
    })
    private Coordinates destination;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "distance_meters", nullable = false)
    private double distanceMeters;

    @Column(name = "duration_seconds", nullable = false)
    private double durationSeconds;

    public void start(Instant startedAt) {
        if (this.status == RouteStatus.IN_PROGRESS) {
            return;
        }

        if (this.status == RouteStatus.CANCELED) {
            throw new BaseApiException(ApiError.ROUTE_ALREADY_CANCELLED);
        }

        if (this.status == RouteStatus.COMPLETED) {
            throw new BaseApiException(ApiError.ROUTE_ALREADY_COMPLETED);
        }

        this.startedAt = startedAt;
        this.status = RouteStatus.IN_PROGRESS;
    }

    public void cancel(Instant cancelledAt) {
        if (this.status == RouteStatus.CANCELED) {
            return;
        }

        this.cancelledAt = cancelledAt;
        this.status = RouteStatus.CANCELED;
    }

    public  void complete(Instant completedAt) {
        if (this.status == RouteStatus.COMPLETED) {
            return;
        }

        this.completedAt = completedAt;
        this.status = RouteStatus.COMPLETED;
    }
}

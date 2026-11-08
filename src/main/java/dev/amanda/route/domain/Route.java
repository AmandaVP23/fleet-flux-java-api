package dev.amanda.route.domain;

import dev.amanda.infrastructure.shared.domain.Coordinates;
import dev.amanda.vehicle.domain.Vehicle;
import jakarta.persistence.*;

import java.time.Instant;

public class Route {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "scheduled_time", nullable = false)
    private Instant scheduledTime;

    @Enumerated(EnumType.STRING)
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
}

package dev.amanda.route.domain;

import dev.amanda.infrastructure.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "route_details")
@Getter
@Setter
public class RouteDetailsEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false, unique = true)
    private Route route;

    @Column(nullable = false, columnDefinition = "text")
    private String geometry;

    // Decimal places of the encoded polyline (6 = polyline6)
    @Column(name = "geometry_precision", nullable = false)
    private int geometryPrecision;

    @Column(name = "distance_meters", nullable = false)
    private double distanceMeters;

    @Column(name = "duration_seconds", nullable = false)
    private double durationSeconds;

    // Meters per segment
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "segment_distances", nullable = false, columnDefinition = "double precision[]")
    private double[] segmentDistancesInMeters;

    // Seconds per segment
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "segment_durations", nullable = false, columnDefinition = "double precision[]")
    private double[] segmentDurationsInSeconds;
}

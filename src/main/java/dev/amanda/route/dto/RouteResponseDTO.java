package dev.amanda.route.dto;

import dev.amanda.infrastructure.shared.application.BaseResponseDTO;
import dev.amanda.infrastructure.shared.domain.Coordinates;
import dev.amanda.route.domain.RouteStatus;
import dev.amanda.vehicle.dto.VehicleSummaryResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class RouteResponseDTO extends BaseResponseDTO {
    private VehicleSummaryResponseDTO vehicle;
    private Instant scheduledTime;
    private RouteStatus status;
    private Coordinates origin;
    private Coordinates destination;
}

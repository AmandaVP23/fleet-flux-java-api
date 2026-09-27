package dev.amanda.vehicle_driver_assignment.dto;

import dev.amanda.shared.application.BaseResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class VehicleDriverAssignmentListResponseDTO extends BaseResponseDTO {
    private Instant startDate;
    private Instant endDate;
    private VehicleDTO vehicle;
    private DriverDTO driver;

    @Getter
    @Setter
    public static class VehicleDTO {
        private Long id;
        private String plateNumber;
        private String brandName;
    }

    @Getter
    @Setter
    public static class DriverDTO {
        private Long id;
        private String firstName;
        private String lastName;
    }
}

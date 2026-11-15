package dev.amanda.tracking_device.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.tracking_device.domain.TrackingDevice;
import dev.amanda.tracking_device.dto.ChangeTrackingDeviceStatusRequestDTO;
import dev.amanda.tracking_device.domain.TrackingDeviceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;
import lombok.RequiredArgsConstructor;

@Log
@ApplicationScoped
@RequiredArgsConstructor
public class ChangeTrackingDeviceStatusUseCase {

    private final TrackingDeviceRepository trackingDeviceRepository;

    public void execute(long trackingDeviceId, ChangeTrackingDeviceStatusRequestDTO dto, AuthContext authContext) {
        TrackingDevice trackingDevice = trackingDeviceRepository.findByIdInOrganizationOrThrow(trackingDeviceId, authContext.getOrganizationId());

        // todo - validate if tracking device is assigned to a vehicle

    }
}

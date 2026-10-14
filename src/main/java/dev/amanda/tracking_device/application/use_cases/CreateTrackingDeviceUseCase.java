package dev.amanda.tracking_device.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.persistence.OrganizationRepositoryPanache;
import dev.amanda.tracking_device.application.mappers.TrackingDeviceMapper;
import dev.amanda.tracking_device.domain.TrackingDevice;
import dev.amanda.tracking_device.dto.CreateTrackingDeviceRequestDTO;
import dev.amanda.tracking_device.dto.TrackingDeviceResponseDTO;
import dev.amanda.tracking_device.persistence.TrackingDeviceRepositoryPanache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

import java.util.Optional;

@Log
@ApplicationScoped
public class CreateTrackingDeviceUseCase {

    @Inject
    TrackingDeviceRepositoryPanache trackingDeviceRepositoryPanache;

    @Inject
    OrganizationRepositoryPanache organizationRepositoryPanache;

    @Inject
    TrackingDeviceMapper trackingDeviceMapper;

    @Transactional
    public TrackingDeviceResponseDTO execute(CreateTrackingDeviceRequestDTO dto, AuthContext authContext) {
        Optional<TrackingDevice> existingDevice = trackingDeviceRepositoryPanache.findByImeiOptional(dto.imei);

        if (existingDevice.isPresent()) {
            throw new BaseApiException(ApiError.TRACKING_DEVICE_IMEI_CONFLICT);
        }

        Organization organization = organizationRepositoryPanache.findByIdOrThrow(authContext.getOrganizationId());

        TrackingDevice trackingDevice = new TrackingDevice();
        trackingDevice.setImei(dto.imei);
        trackingDevice.setModel(dto.model);
        trackingDevice.setOrganization(organization);
        trackingDevice.setStatus(dto.status);

        trackingDeviceRepositoryPanache.persist(trackingDevice);

        return trackingDeviceMapper.toDto(trackingDevice);
    }
}

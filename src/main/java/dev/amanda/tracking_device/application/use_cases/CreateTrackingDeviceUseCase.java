package dev.amanda.tracking_device.application.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.tracking_device.application.mappers.TrackingDeviceMapper;
import dev.amanda.tracking_device.domain.TrackingDevice;
import dev.amanda.tracking_device.dto.CreateTrackingDeviceRequestDTO;
import dev.amanda.tracking_device.dto.TrackingDeviceResponseDTO;
import dev.amanda.tracking_device.domain.TrackingDeviceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

import java.util.Optional;

@Log
@ApplicationScoped
public class CreateTrackingDeviceUseCase {

    @Inject
    TrackingDeviceRepository trackingDeviceRepository;

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    TrackingDeviceMapper trackingDeviceMapper;

    @Transactional
    public TrackingDeviceResponseDTO execute(CreateTrackingDeviceRequestDTO dto, AuthContext authContext) {
        Optional<TrackingDevice> existingDevice = trackingDeviceRepository.findByImeiAndOrganizationOptional(dto.imei, authContext.getOrganizationId());

        if (existingDevice.isPresent()) {
            throw new BaseApiException(ApiError.TRACKING_DEVICE_IMEI_CONFLICT);
        }

        Organization organization = organizationRepository.findByIdOrThrow(authContext.getOrganizationId());

        TrackingDevice trackingDevice = new TrackingDevice();
        trackingDevice.setImei(dto.imei);
        trackingDevice.setModel(dto.model);
        trackingDevice.setOrganization(organization);
        trackingDevice.setStatus(dto.status);

        trackingDeviceRepository.persist(trackingDevice);

        return trackingDeviceMapper.toDto(trackingDevice);
    }
}

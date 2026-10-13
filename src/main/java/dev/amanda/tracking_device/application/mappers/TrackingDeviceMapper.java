package dev.amanda.tracking_device.application.mappers;

import dev.amanda.infrastructure.shared.application.BaseMapper;
import dev.amanda.tracking_device.domain.TrackingDevice;
import dev.amanda.tracking_device.dto.TrackingDeviceResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta", uses = BaseMapper.class)
public interface TrackingDeviceMapper {
    TrackingDeviceResponseDTO toDto(TrackingDevice trackingDevice);
}

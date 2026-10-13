package dev.amanda.tracking_device.domain;


import java.util.Optional;

public interface TrackingDeviceRepository {
    Optional<TrackingDevice> findByImeiOptional(String imei);
}

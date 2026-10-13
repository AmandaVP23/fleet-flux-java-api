package dev.amanda.tracking_device.persistence;

import dev.amanda.tracking_device.domain.TrackingDevice;
import dev.amanda.tracking_device.domain.TrackingDeviceRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class TrackingDeviceRepositoryPanache implements TrackingDeviceRepository, PanacheRepository<TrackingDevice> {
    @Override
    public Optional<TrackingDevice> findByImeiOptional(String imei) {
        return find("imei", imei).firstResultOptional();
    }
}

package dev.amanda.tracking_device.persistence;

import dev.amanda.infrastructure.shared.exception.ApiError;
import dev.amanda.infrastructure.shared.exception.BaseApiException;
import dev.amanda.tracking_device.domain.TrackingDevice;
import dev.amanda.tracking_device.domain.TrackingDeviceRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TrackingDeviceRepositoryPanache implements TrackingDeviceRepository, PanacheRepository<TrackingDevice> {

    @Override
    public Optional<TrackingDevice> findByImeiAndOrganizationOptional(String imei, long organizationId) {
        return find("imei = ?1 AND organization.id = ?2", imei, organizationId).firstResultOptional();
    }

    @Override
    public List<TrackingDevice> findPaginated(int page, int size, Sort sort, long organizationId) {
        return find("organization.id = ?1", sort, organizationId)
                .page(Page.of(page, size))
                .list();
    }

    @Override
    public long count(long organizationId) {
        return count("organization.id = ?1", organizationId);
    }

    @Override
    public TrackingDevice findByIdInOrganizationOrThrow(long id, long organizationId) {
        return find("id = ?1 and organization.id = ?2", id, organizationId)
                .firstResultOptional()
                .orElseThrow(() -> new BaseApiException(ApiError.TRACKING_DEVICE_NOT_FOUND));
    }
}

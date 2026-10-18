package dev.amanda.tracking_device.domain;


import io.quarkus.panache.common.Sort;

import java.util.List;
import java.util.Optional;

public interface TrackingDeviceRepository {
    Optional<TrackingDevice> findByImeiAndOrganizationOptional(String imei, long organizationId);

    List<TrackingDevice> findPaginated(int page, int size, Sort sort, long organizationId);

    long count(long organizationId);
}

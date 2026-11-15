package dev.amanda.tracking_device.application.use_cases;

import dev.amanda.infrastructure.shared.PageResult;
import dev.amanda.infrastructure.shared.application.PageRequestHelper;
import dev.amanda.tracking_device.application.mappers.TrackingDeviceMapper;
import dev.amanda.tracking_device.dto.TrackingDeviceResponseDTO;
import dev.amanda.tracking_device.domain.TrackingDeviceRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@Log
@ApplicationScoped
@RequiredArgsConstructor
public class ListTrackingDevicesUseCase {

    private final TrackingDeviceRepository trackingDeviceRepository;
    private final TrackingDeviceMapper trackingDeviceMapper;
    private final PageRequestHelper pageRequestHelper;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "imei", "createdAt");

    public PageResult<TrackingDeviceResponseDTO> execute(int page, int size, String sortBy, String sortDirection, long organizationId) {
        pageRequestHelper.validate(page, size, sortBy, sortDirection, ALLOWED_SORT_FIELDS);

        Sort sort = pageRequestHelper.buildSort(sortBy, sortDirection);

        long total = trackingDeviceRepository.count(organizationId);

        List<TrackingDeviceResponseDTO> data = trackingDeviceRepository
                .findPaginated(page, size, sort, organizationId)
                .stream()
                .map(trackingDeviceMapper::toDto)
                .toList();

        return new PageResult<>(data, total, page, size);
    }
}

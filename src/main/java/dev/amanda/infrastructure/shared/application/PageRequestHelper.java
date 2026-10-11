package dev.amanda.infrastructure.shared.application;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

import java.util.Set;

@ApplicationScoped
public class PageRequestHelper {

    private static final int MAX_PAGE_SIZE = 100;

    public void validate(int pageNumber, int pageSize, String sortBy, String direction, Set<String> allowedSortFields) {
        if (pageNumber < 0) {
            throw new BadRequestException("'page' must be >= 0");
        }

        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new BadRequestException("'size' must be between 1 and " + MAX_PAGE_SIZE);
        }

        if (allowedSortFields != null && !allowedSortFields.contains(sortBy)) {
            throw new BadRequestException("'sortBy' must be one of: " + allowedSortFields);
        }

        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new BadRequestException("'direction' must be 'asc' or 'desc'");
        }
    }

    public Sort buildSort(String sortBy, String direction) {
        return direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
    }
}

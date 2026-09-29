package dev.amanda.vehicle_brand.persistence;

import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.domain.VehicleBrandRepository;
import dev.amanda.vehicle_brand.exceptions.VehicleBrandNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VehicleBrandRepositoryPanache implements VehicleBrandRepository, PanacheRepository<VehicleBrand> {

    @Override
    public VehicleBrand findByIdOrThrow(Long id) {
        Optional<VehicleBrand> vehicleBrand = this.findByIdOptional(id);

        if (vehicleBrand.isEmpty()) {
            throw new VehicleBrandNotFoundException();
        }

        return vehicleBrand.get();
    }

    @Override
    public VehicleBrand findActiveByIdOrThrow(Long id) {
        Optional<VehicleBrand> vehicleBrand = this.findByIdOptional(id);

        if (vehicleBrand.isEmpty() || vehicleBrand.get().getDeletedAt() != null) {
            throw new VehicleBrandNotFoundException();
        }

        return vehicleBrand.get();
    }

    @Override
    public List<VehicleBrand> listAllDeleted() {
        return find("deletedAt IS NOT NULL")
                .list();
    }

    @Override
    public List<VehicleBrand> listAllActive() {
        return find("deletedAt IS NULL")
                .list();
    }
}

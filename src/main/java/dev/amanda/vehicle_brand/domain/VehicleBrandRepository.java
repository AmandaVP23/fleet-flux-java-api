package dev.amanda.vehicle_brand.domain;

import dev.amanda.vehicle_brand.exceptions.VehicleBrandNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class VehicleBrandRepository implements PanacheRepository<VehicleBrand> {
    public VehicleBrand findByIdOrThrow(Long id) {
        Optional<VehicleBrand> vehicleBrand = this.findByIdOptional(id);

        if (vehicleBrand.isEmpty()) {
            throw new VehicleBrandNotFoundException();
        }

        return vehicleBrand.get();
    }
}

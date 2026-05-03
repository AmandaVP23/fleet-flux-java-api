package dev.amanda.vehicle_brand.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VehicleBrandRepository implements PanacheRepository<VehicleBrand> {
}

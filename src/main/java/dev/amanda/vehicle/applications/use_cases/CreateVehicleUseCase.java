package dev.amanda.vehicle.applications.use_cases;

import dev.amanda.infrastructure.oidc.AuthContext;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.persistence.OrganizationRepositoryPanache;
import dev.amanda.vehicle.applications.mappers.VehicleMapper;
import dev.amanda.vehicle.domain.*;
import dev.amanda.vehicle.dto.CreateVehicleRequestDTO;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import dev.amanda.vehicle.persistence.VehicleRepositoryPanache;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.persistence.VehicleBrandRepositoryPanache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateVehicleUseCase {

    @Inject
    OrganizationRepositoryPanache organizationRepositoryPanache;

    @Inject
    VehicleBrandRepositoryPanache vehicleBrandRepositoryPanache;

    @Inject
    VehicleRepositoryPanache vehicleRepositoryPanache;

    @Inject
    VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponseDTO execute(CreateVehicleRequestDTO dto, AuthContext authContext) {
        long effectiveOrgId;

        if (authContext.isSuperAdmin()) {
            effectiveOrgId = dto.organizationId();
        } else {
            effectiveOrgId = authContext.getOrganizationId();
        }

        Organization organization = organizationRepositoryPanache.findActiveByIdOrThrow(effectiveOrgId);
        VehicleBrand vehicleBrand = vehicleBrandRepositoryPanache.findActiveByIdOrThrow(dto.brandId());

        Vehicle vehicle = new Vehicle();
        vehicleMapper.updateVehicleFromDto(dto, vehicle);
        vehicle.setOrganization(organization);
        vehicle.setBrand(vehicleBrand);

        vehicleRepositoryPanache.persist(vehicle);

        return vehicleMapper.toDto(vehicle);
    }
}

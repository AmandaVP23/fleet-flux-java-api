package dev.amanda.vehicle.applications.use_cases;

import dev.amanda.oidc.AuthContext;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.vehicle.applications.mappers.VehicleMapper;
import dev.amanda.vehicle.domain.*;
import dev.amanda.vehicle.dto.CreateVehicleRequestDTO;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.domain.VehicleBrandRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateVehicleUseCase {

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    VehicleBrandRepository vehicleBrandRepository;

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponseDTO execute(CreateVehicleRequestDTO dto, AuthContext authContext) {
        Long effectiveOrgId;

        if (authContext.isSuperAdmin()) {
            effectiveOrgId = dto.organizationId();
        } else {
            effectiveOrgId = authContext.getOrganizationId();
        }

        Organization organization = organizationRepository.findActiveByIdOrThrow(effectiveOrgId);
        VehicleBrand vehicleBrand = vehicleBrandRepository.findActiveByIdOrThrow(dto.brandId());

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleBrand(vehicleBrand);
        vehicle.setOrganization(organization);
        vehicle.setModel(dto.model());
        vehicle.setVin(dto.vin());
        vehicle.setCategory(dto.category());
        vehicle.setVariant(dto.variant());
        vehicle.setPlateNumber(dto.plateNumber());
        vehicle.setYearOfManufacture(dto.yearOfManufacture());
        vehicle.setType(dto.type());
        vehicle.setFuelType(dto.fuelType());
        vehicle.setFuelCapacityInLiters(dto.fuelCapacityInLiters());
        vehicle.setStatus(dto.status());
        vehicle.setAvgConsumptionPer100km(dto.avgConsumptionPer100Km());
        vehicle.setLastServiceDate(dto.lastServiceDate());
        vehicle.setInsurancePolicyNumber(dto.insurancePolicyNumber());
        vehicle.setInsuranceExpiryDate(dto.insuranceExpiryDate());
        vehicle.setInspectionDueDate(dto.inspectionDueDate());
        vehicle.setOwnershipType(dto.ownershipType());

        vehicleRepository.persist(vehicle);

        return vehicleMapper.toDto(vehicle);
    }
}

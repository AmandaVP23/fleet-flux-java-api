package dev.amanda.vehicle.applications.mappers;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.shared.application.BaseMapper;
import dev.amanda.vehicle.domain.Vehicle;
import dev.amanda.vehicle.dto.CreateVehicleRequestDTO;
import dev.amanda.vehicle.dto.VehicleResponseDTO;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "cdi", uses = BaseMapper.class)
public interface VehicleMapper {

    @Mapping(target = "organization", source = "organization")
    @Mapping(target = "brand", source = "brand")
    VehicleResponseDTO toDto(Vehicle vehicle);

    OrganizationResponseDTO toDto(Organization organization);

    VehicleBrandResponseDTO toDto(VehicleBrand vehicleBrand);

    void updateVehicleFromDto(
            CreateVehicleRequestDTO dto,
            @MappingTarget Vehicle vehicle
    );
}

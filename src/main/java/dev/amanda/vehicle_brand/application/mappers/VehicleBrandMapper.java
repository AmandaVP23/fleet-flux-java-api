package dev.amanda.vehicle_brand.application.mappers;

import dev.amanda.infrastructure.shared.application.BaseMapper;
import dev.amanda.vehicle_brand.domain.VehicleBrand;
import dev.amanda.vehicle_brand.dto.VehicleBrandResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta", uses = BaseMapper.class)
public interface VehicleBrandMapper {

    VehicleBrandResponseDTO toDto(VehicleBrand vehicleBrand);
}

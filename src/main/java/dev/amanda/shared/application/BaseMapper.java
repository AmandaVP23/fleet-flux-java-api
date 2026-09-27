package dev.amanda.shared.application;

import dev.amanda.shared.domain.BaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface BaseMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    BaseResponseDTO mapBase(BaseEntity entity);
}

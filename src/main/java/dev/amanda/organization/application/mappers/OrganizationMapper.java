package dev.amanda.organization.application.mappers;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.infrastructure.shared.application.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta", uses = BaseMapper.class)
public interface OrganizationMapper {
    OrganizationResponseDTO toDto(Organization organization);
}

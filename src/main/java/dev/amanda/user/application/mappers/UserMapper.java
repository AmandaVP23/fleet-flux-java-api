package dev.amanda.user.application.mappers;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.shared.application.BaseMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi", uses = BaseMapper.class)
public interface UserMapper {

    @Mapping(target = "organization", source = "organization")
    UserResponseDTO toDto(User user);

    OrganizationResponseDTO toDto(Organization organization);
}

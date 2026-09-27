package dev.amanda.user.application.mappers;

import dev.amanda.organization.application.mappers.OrganizationMapper;
import dev.amanda.shared.application.BaseMapper;
import dev.amanda.user.domain.User;
import dev.amanda.user.dto.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta", uses = { BaseMapper.class, OrganizationMapper.class})
public interface UserMapper {

    UserResponseDTO toDto(User user);

}

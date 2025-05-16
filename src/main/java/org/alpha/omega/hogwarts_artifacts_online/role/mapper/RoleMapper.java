package org.alpha.omega.hogwarts_artifacts_online.role.mapper;

import org.alpha.omega.hogwarts_artifacts_online.entity.Role;
import org.alpha.omega.hogwarts_artifacts_online.response.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDTO toRoleDTO(Role role);
}

package org.alpha.omega.hogwarts_artifacts_online.response.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserDTO(
        Integer id,
        String description,
        Boolean enabled,
        String username,
        String password,
        Set<RoleDTO> roles
) {
}

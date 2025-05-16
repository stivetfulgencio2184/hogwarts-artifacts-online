package org.alpha.omega.hogwarts_artifacts_online.response.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserDTO(
        Integer id,
        Boolean enabled,
        String username,
        Set<RoleDTO> roles
) {
}

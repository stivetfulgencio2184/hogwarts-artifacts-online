package org.alpha.omega.hogwarts_artifacts_online.response.dto;

import lombok.Builder;

@Builder
public record RoleDTO(
        Integer id,
        String name
) {
}

package org.alpha.omega.hogwarts_artifacts_online.response.dto;

import lombok.Builder;

@Builder
public record WizardDTO(
        Long id,
        String name,
        Integer numberOfArtifacts
) {
}

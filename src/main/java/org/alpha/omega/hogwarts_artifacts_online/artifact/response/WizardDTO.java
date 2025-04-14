package org.alpha.omega.hogwarts_artifacts_online.artifact.response;

import lombok.Builder;

@Builder
public record WizardDTO(
        Long id,
        String name,
        Integer numberOfArtifacts
) {
}

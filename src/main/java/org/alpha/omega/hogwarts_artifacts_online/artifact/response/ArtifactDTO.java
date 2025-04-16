package org.alpha.omega.hogwarts_artifacts_online.artifact.response;

import lombok.Builder;

@Builder
public record ArtifactDTO(
        String id,
        String name,
        String description,
        String imageUrl,
        WizardDTO owner
) {
}

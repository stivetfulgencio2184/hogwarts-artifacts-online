package org.alpha.omega.hogwarts_artifacts_online.artifact.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ArtifactRequest(
        @NotNull(message = "The artifact name is required.")
        @NotEmpty(message = "The artifact name should not empty.")
        String name,

        @NotNull(message = "The artifact description is required.")
        @NotEmpty(message = "The artifact description should not empty.")
        String description,

        @NotNull(message = "The artifact image url is required.")
        @NotEmpty(message = "The artifact image url should not empty.")
        String imageUrl
) {
}

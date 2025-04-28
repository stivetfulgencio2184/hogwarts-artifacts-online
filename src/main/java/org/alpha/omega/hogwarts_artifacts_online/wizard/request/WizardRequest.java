package org.alpha.omega.hogwarts_artifacts_online.wizard.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WizardRequest(
        @NotNull(message = "The wizard name is required.")
        @NotEmpty(message = "The wizard name should not empty.")
        String name
) {
}

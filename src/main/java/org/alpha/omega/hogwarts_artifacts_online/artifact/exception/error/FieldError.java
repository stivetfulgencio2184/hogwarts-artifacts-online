package org.alpha.omega.hogwarts_artifacts_online.artifact.exception.error;

import lombok.Builder;

@Builder
public record FieldError (
        String field,
        String message) {

}

package org.alpha.omega.hogwarts_artifacts_online.user.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRequestUpdt(

        Boolean enabled,

        @NotNull(message = "User name is required.")
        @NotEmpty(message = "User name should not empty.")
        @Size(message = "The maximum length for user name is 30.", max = 30)
        String username
) {
}

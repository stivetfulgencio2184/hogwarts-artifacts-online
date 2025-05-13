package org.alpha.omega.hogwarts_artifacts_online.user.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRequest(
        @NotNull(message = "Description user is required.")
        @NotEmpty(message = "Description user should not empty.")
        @Size(message = "The maximum length for description user is 255.", max = 255)
        String description,

        Boolean enabled,

        @NotNull(message = "User name is required.")
        @NotEmpty(message = "User name should not empty.")
        @Size(message = "The maximum length for user name is 30.", max = 30)
        String username,

        @NotNull(message = "User password is required.")
        @NotEmpty(message = "User password should not empty.")
        @Size(message = "The maximum length for user password is 30.", max = 30)
        String password
) {
}

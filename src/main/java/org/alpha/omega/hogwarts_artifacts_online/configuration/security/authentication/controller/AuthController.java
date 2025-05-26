package org.alpha.omega.hogwarts_artifacts_online.configuration.security.authentication.controller;

import org.alpha.omega.hogwarts_artifacts_online.configuration.security.authentication.service.AuthService;
import org.alpha.omega.hogwarts_artifacts_online.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${api.endpoint.base-url.v1}/users")
public class AuthController {

    private final AuthService authService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getLoginInfo(Authentication authentication) {
        LOGGER.debug("Authenticated user: '{}'", authentication.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Result.builder()
                        .flag(Boolean.TRUE)
                        .code(HttpStatus.OK.value())
                        .message("User info and JSON Web Token")
                        .data(this.authService.createLoginInfo(authentication))
                        .build());
    }
}

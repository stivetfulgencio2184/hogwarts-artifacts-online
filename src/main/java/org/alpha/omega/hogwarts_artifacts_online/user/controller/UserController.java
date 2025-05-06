package org.alpha.omega.hogwarts_artifacts_online.user.controller;

import lombok.RequiredArgsConstructor;
import org.alpha.omega.hogwarts_artifacts_online.response.Result;
import org.alpha.omega.hogwarts_artifacts_online.user.mapper.UserMapper;
import org.alpha.omega.hogwarts_artifacts_online.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${api.endpoint.base-url.v1}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getUserById(@PathVariable(name = "userId") Integer id) {
        return ResponseEntity.ok(Result.builder()
                        .flag(Boolean.TRUE)
                        .code(HttpStatus.OK.value())
                        .message("Find success")
                        .data(UserMapper.INSTANCE.toUserDto(this.userService.findUserById(id)))
                .build());
    }
}

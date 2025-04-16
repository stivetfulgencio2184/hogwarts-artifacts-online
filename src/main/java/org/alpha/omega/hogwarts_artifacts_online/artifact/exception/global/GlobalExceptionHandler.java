package org.alpha.omega.hogwarts_artifacts_online.artifact.exception.global;

import org.alpha.omega.hogwarts_artifacts_online.artifact.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.artifact.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Result handleNotFoundException(NotFoundException exception) {
        return Result.builder()
                .flag(Boolean.FALSE)
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
    }
}

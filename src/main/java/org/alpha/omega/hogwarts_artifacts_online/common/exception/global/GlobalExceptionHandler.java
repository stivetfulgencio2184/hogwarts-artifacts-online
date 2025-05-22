package org.alpha.omega.hogwarts_artifacts_online.common.exception.global;

import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.AlreadyRegisteredException;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.error.FieldError;
import org.alpha.omega.hogwarts_artifacts_online.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

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

    @ExceptionHandler(value = {AlreadyRegisteredException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Result handleAlreadyRegisteredException(AlreadyRegisteredException exception) {
        return Result.builder()
                .flag(Boolean.FALSE)
                .code(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(Result.builder()
                .flag(Boolean.FALSE)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(Constant.CustomExMessage.INVALID_ARGUMENTS)
                .data(exception.getFieldErrors().stream()
                        .filter(Objects::nonNull)
                        .map(fieldError -> FieldError.builder()
                                .field(fieldError.getField())
                                .message(fieldError.getDefaultMessage())
                                .build())
                        .toList())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Result handleAuthenticationException(Exception exception) {
        return Result.builder()
                .flag(Boolean.FALSE)
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(exception.getMessage())
                .build();
    }
}

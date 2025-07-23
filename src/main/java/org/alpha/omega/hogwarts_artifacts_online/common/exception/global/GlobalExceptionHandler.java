package org.alpha.omega.hogwarts_artifacts_online.common.exception.global;

import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.AlreadyRegisteredException;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.error.FieldError;
import org.alpha.omega.hogwarts_artifacts_online.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
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

    /**
     * Spring Security Exceptions
     * - Authentication Exceptions: UsernameNotFoundException and BadCredentialsException
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Result handleAuthenticationException(Exception exception) {
        return Result.builder()
                .flag(Boolean.FALSE)
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(Constant.Security.ExceptionMessage.USERNAME_PASSWORD_INCORRECT)
                .data(exception.getMessage())
                .build();
    }

    /**
     * Spring Security Exceptions
     * - Authentication Exceptions: InsufficientAuthenticationException
     * Launch a get request to wizards, without No auth
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {InsufficientAuthenticationException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Result handleInsufficientAuthenticationException(InsufficientAuthenticationException exception) {
        return Result.builder()
                .flag(Boolean.FALSE)
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(Constant.Security.ExceptionMessage.LOGIN_CREDENTIALS_MISSING)
                .data(exception.getMessage())
                .build();
    }

    /**
     * Spring Security Exceptions
     * - Authentication Exceptions: AccountStatusException
     * A user with username and password correctly authenticated, but whose Enabled field is false, intent login.
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {AccountStatusException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Result handleAccountStatusException(AccountStatusException exception) {
        return Result.builder()
                .flag(Boolean.FALSE)
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(Constant.Security.ExceptionMessage.USER_ACCOUNT_ABNORMAL)
                .data(exception.getMessage())
                .build();
    }

    /**
     * Spring Security Exceptions
     * - Authentication Exceptions: InvalidBearerTokenException
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {InvalidBearerTokenException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Result handleInvalidBearerTokenException(InvalidBearerTokenException exception) {
        return Result.builder()
                .flag(Boolean.FALSE)
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(Constant.Security.ExceptionMessage.INVALID_BEARER_TOKEN)
                .data(exception.getMessage())
                .build();
    }

    /**
     * Spring Security Exceptions
     * - Authorization Exception: AccessDeniedException
     * A user with username and password correctly authenticated intent execute an action about which not have permission.
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public Result handleAccessDeniedException(AccessDeniedException exception) {
        return Result.builder()
                .flag(Boolean.FALSE)
                .code(HttpStatus.FORBIDDEN.value())
                .message(Constant.Security.ExceptionMessage.NO_PERMISSION)
                .data(exception.getMessage())
                .build();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleInternalServerErrorException(Exception exception){
        return Result.builder()
                .flag(Boolean.FALSE)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(Constant.Security.ExceptionMessage.INTERNAL_SERVER_ERROR)
                .data(exception.getMessage())
                .build();
    }
}

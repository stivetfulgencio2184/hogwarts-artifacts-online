package org.alpha.omega.hogwarts_artifacts_online.configuration.security.authentication.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * This class handle unsuccessful JWT authorization
 */
@Component
public class CustomBearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Here we've injected the default HandlerExceptionResolver and delegated the handler to this resolver.
     * This security exception can now be handled with: @ControllerAdvice or @RestControllerAdvice with on exception
     * handler method (@ExceptionHandler).
     */
    private final HandlerExceptionResolver resolver;

    public CustomBearerTokenAuthenticationEntryPoint(@Qualifier(value = "handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.resolver.resolveException(request, response, null, authException);
    }
}

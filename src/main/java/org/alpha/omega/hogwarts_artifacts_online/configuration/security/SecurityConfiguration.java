package org.alpha.omega.hogwarts_artifacts_online.configuration.security;

import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.alpha.omega.hogwarts_artifacts_online.role.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {

    @Value(value = "${api.endpoint.base-url.v1}")
    private String baseUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // Authorization URLs
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/artifacts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + Constant.Security.WILDCARD_URL_USER).hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + Constant.Security.WILDCARD_URL_USER).hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + Constant.Security.WILDCARD_URL_USER).hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        // Others URLs will require Authentication. Urls not public
                        .anyRequest().authenticated() // Always a good idea to put this as last. Disallow everything else
                )
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // This is for H2 browser console access.
                .csrf(AbstractHttpConfigurer::disable) // Cross-Site Request Forgery
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // BCryptPasswordEncoder is an: One-Way Hashing Function
    }
}

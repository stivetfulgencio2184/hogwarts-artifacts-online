package org.alpha.omega.hogwarts_artifacts_online.configuration.security.authentication.provider;

import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value(value = "${jwt.expiresIn}")
    private Long expiresIn;

    private final JwtEncoder jwtEncoder;

    public JwtProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                        .issuer(Constant.Security.Jwt.ISSUER)
                        .issuedAt(now)
                        .expiresAt(now.plus(this.expiresIn, ChronoUnit.HOURS))
                        .subject(authentication.getName())
                        .claim(Constant.Security.Jwt.CLAIM_NAME, joinGrantedAuthorities(authentication.getAuthorities()))
                        .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String joinGrantedAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

}

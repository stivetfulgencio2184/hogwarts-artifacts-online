package org.alpha.omega.hogwarts_artifacts_online.configuration.security.authentication.service;

import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.alpha.omega.hogwarts_artifacts_online.configuration.security.MyUserPrincipal;
import org.alpha.omega.hogwarts_artifacts_online.configuration.security.authentication.provider.JwtProvider;
import org.alpha.omega.hogwarts_artifacts_online.user.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    public AuthService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        MyUserPrincipal userPrincipal = (MyUserPrincipal) authentication.getPrincipal();

        return Map.of(Constant.Security.USER_INFO, UserMapper.INSTANCE.toUserDto(userPrincipal.user()),
                    Constant.Security.TOKEN, this.jwtProvider.createToken(authentication));
    }
}

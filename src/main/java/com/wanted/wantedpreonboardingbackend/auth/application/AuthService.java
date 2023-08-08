package com.wanted.wantedpreonboardingbackend.auth.application;

import com.wanted.wantedpreonboardingbackend.auth.dto.TokenRequest;
import com.wanted.wantedpreonboardingbackend.auth.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(TokenRequest tokenRequest) {
        //Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                tokenRequest.getEmail(),
                tokenRequest.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //검증된 인증 정보로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }
}

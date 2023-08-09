package com.wanted.wantedpreonboardingbackend.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.wanted.wantedpreonboardingbackend.auth.dto.TokenRequest;
import com.wanted.wantedpreonboardingbackend.auth.dto.TokenResponse;
import com.wanted.wantedpreonboardingbackend.auth.infrastructure.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("로그인 할 수 있다")
    void login() {
        // Given
        TokenRequest tokenRequest = new TokenRequest("test@example.com", "password");
        TokenResponse expectedResponse = TokenResponse.builder()
                .accessToken("TOKEN")
                .build();
        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);
        given(authenticationManager.authenticate(any())).willReturn(authentication);
        given(jwtTokenProvider.generateToken(authentication)).willReturn(expectedResponse);

        // When
        TokenResponse actualResponse = authService.login(tokenRequest);

        // Then
        assertThat(actualResponse.getAccessToken()).isEqualTo(expectedResponse.getAccessToken());
    }
}

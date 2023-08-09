package com.wanted.wantedpreonboardingbackend.auth.ui;

import com.wanted.wantedpreonboardingbackend.auth.application.AuthService;
import com.wanted.wantedpreonboardingbackend.auth.dto.TokenResponse;
import com.wanted.wantedpreonboardingbackend.auth.dto.TokenRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid TokenRequest tokenRequest) {
        TokenResponse token = authService.login(tokenRequest);
        return ResponseEntity.ok().body(token);
    }
}


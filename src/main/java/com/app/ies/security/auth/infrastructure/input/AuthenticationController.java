package com.app.ies.security.auth.infrastructure.input;

import com.app.ies.security.auth.application.dtos.auth.LoginRequest;
import com.app.ies.security.auth.application.dtos.auth.LoginResponse;
import com.app.ies.security.auth.application.dtos.auth.RefreshTokenRequest;
import com.app.ies.security.auth.application.handlers.auth.AuthenticationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationHandler authenticationHandler;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationHandler.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authenticationHandler.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {
        authenticationHandler.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

}

package com.app.ies.security.auth.infrastructure.input;

import com.app.ies.security.auth.application.dtos.auth.LoginRequest;
import com.app.ies.security.auth.application.dtos.auth.LoginResponse;
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

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader("refresh-token")  String refreshToken) {
        return ResponseEntity.ok(authenticationHandler.refreshToken(refreshToken));
    }

}

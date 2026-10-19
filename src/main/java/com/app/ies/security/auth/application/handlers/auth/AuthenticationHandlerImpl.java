package com.app.ies.security.auth.application.handlers.auth;

import com.app.ies.security.auth.application.dtos.auth.LoginRequest;
import com.app.ies.security.auth.application.dtos.auth.LoginResponse;
import com.app.ies.security.auth.domain.api.AuthenticationServicePort;
import com.app.ies.security.auth.domain.models.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationHandlerImpl implements AuthenticationHandler {

    private final AuthenticationServicePort authenticationServicePort;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Token token = authenticationServicePort.login(loginRequest.getUsername(), loginRequest.getPassword());
        return LoginResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .type(token.getType())
                //.username(token.getUsername())
                .build();
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        Token token = authenticationServicePort.refreshToken(refreshToken);
        return LoginResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .type(token.getType())
                //.username(token.getUsername())
                .build();
    }
}

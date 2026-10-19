package com.app.ies.security.auth.application.handlers.auth;


import com.app.ies.security.auth.application.dtos.auth.LoginRequest;
import com.app.ies.security.auth.application.dtos.auth.LoginResponse;

public interface AuthenticationHandler {

    LoginResponse login(LoginRequest loginRequest);

    LoginResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

}

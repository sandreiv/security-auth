package com.app.ies.security.auth.domain.api;


import com.app.ies.security.auth.domain.models.Token;

public interface AuthenticationServicePort {

    Token login(String username, String password);

    Token refreshToken(String refreshToken);

    void logout(String refreshToken);

}

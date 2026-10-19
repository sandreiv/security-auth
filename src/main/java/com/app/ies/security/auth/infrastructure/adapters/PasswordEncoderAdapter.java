package com.app.ies.security.auth.infrastructure.adapters;

import com.app.ies.security.auth.domain.ports.security.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean matches(String plainString, String hashString, String idUsuario, String idPersona) {
        return passwordEncoder.matches(plainString, hashString);
    }

    @Override
    public String encode(String plainString) {
        return passwordEncoder.encode(plainString);
    }

}

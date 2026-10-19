package com.app.ies.security.auth.infrastructure.adapters;

import com.app.ies.security.auth.domain.ports.security.PasswordEncoderPort;
import com.app.ies.security.auth.infrastructure.security.adapters.ILegacyPbeValidatorAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class HybridPasswordEncoderAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ILegacyPbeValidatorAdapter legacyPbeValidatorAdapter;

    //private final String secretKeyLegacy = "Vortal Hermesoft - Plataforma - Universidad de Pamplona";

    @Override
    public boolean matches(String plainString, String hashString, String idUsuario, String idPersona) {
        System.out.println("::::::::::::::::::::::::>HybridPasswordEncoderAdapter1");
        if (hashString != null && hashString.startsWith("$2")) {
            return bCryptPasswordEncoder.matches(plainString, hashString);
        }
        return legacyPbeValidatorAdapter.verify(plainString, hashString, idUsuario, idPersona);
    }

    @Override
    public String encode(String plainString) {
        return bCryptPasswordEncoder.encode(plainString);
    }

}

package com.app.ies.security.auth.domain.ports.security;

public interface PasswordEncoderPort {

    boolean matches(String claveEntra, String claveBase, String idUsuario, String idPersona);

    String encode(String plainString);

}

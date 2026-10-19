package com.app.ies.security.auth.infrastructure.security.adapters;

public interface ILegacyPbeValidatorAdapter {

    boolean verify(String rawPassword, String hexEncodedPassword, String idUsuario, String idPersona);

}

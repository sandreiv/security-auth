package com.app.ies.security.auth.domain.usecases;


import com.app.ies.security.auth.domain.api.AuthenticationServicePort;
import com.app.ies.security.auth.domain.exceptions.AccessDeniedException;
import com.app.ies.security.auth.domain.exceptions.UnauthorizedException;
import com.app.ies.security.auth.domain.models.RefreshToken;
import com.app.ies.security.auth.domain.models.Rol;
import com.app.ies.security.auth.domain.models.AplicacionRoles;
import com.app.ies.security.auth.domain.models.Token;
import com.app.ies.security.auth.domain.models.UserAuth;
import com.app.ies.security.auth.domain.ports.persistence.RefreshTokenPersistencePort;
import com.app.ies.security.auth.domain.ports.persistence.RolPersistencePort;
import com.app.ies.security.auth.domain.ports.persistence.UserAuthPersistencePort;
import com.app.ies.security.auth.domain.ports.security.PasswordEncoderPort;
import com.app.ies.security.auth.domain.ports.security.TokenProviderPort;

import java.time.LocalDateTime;
import java.util.List;


public class AuthenticationUseCase implements AuthenticationServicePort {

    private final UserAuthPersistencePort userAuthPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenPersistencePort refreshTokenPersistencePort;
    private final RolPersistencePort rolPersistencePort;

    public AuthenticationUseCase(
            UserAuthPersistencePort userAuthPersistencePort,
            PasswordEncoderPort passwordEncoderPort,
            TokenProviderPort tokenProviderPort,
            RefreshTokenPersistencePort refreshTokenPersistencePort,
            RolPersistencePort rolPersistencePort)
    {
        this.userAuthPersistencePort = userAuthPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
        this.refreshTokenPersistencePort = refreshTokenPersistencePort;
        this.rolPersistencePort = rolPersistencePort;
    }

    @Override
    public Token login(String username, String password) {
        UserAuth user = userAuthPersistencePort.findByUsername(username).orElseThrow( () -> new AccessDeniedException("Credenciales Inválidas") );
        List<Rol> roles = rolPersistencePort.rolesPorUsuario(user.getId());

        if(roles.isEmpty() || roles == null) {
            throw new AccessDeniedException("El usuario no tiene roles asignados");
        }
        List<String> rolesString = roles.stream()
                .map(Rol::getNombre)
                .toList();

        // Lo que obtuve hace match con el password del usuario? Verifico con el passwordEncoderPort
        if(!passwordEncoderPort.matches(password, user.getPassword(), String.valueOf(user.getId()), user.getIdPersona()) ) {
            throw new AccessDeniedException("Credenciales Inválidas");
        }
        // obtener aplicaciones con roles por usuario y generar token con esa estructura
        List<AplicacionRoles> aplicacionesRoles = rolPersistencePort.aplicacionesRolesPorUsuario(user.getId());
        String token = tokenProviderPort.generateToken(
                user, rolesString, aplicacionesRoles);
        //RefreshToken refreshToken = tokenProviderPort.getRefreshToken(user.getId());
        //refreshTokenPersistencePort.save(refreshToken);

        //return new Token(token, "refreshToken.getRefreshToken()", "Bearer", user.getUsername());
        return new Token(token, "refreshToken.getRefreshToken()", "Bearer");

    }

    @Override
    public Token refreshToken(String refreshToken) {

        String[] arrayRefreshToken = refreshToken.split("\\.");
        if(arrayRefreshToken.length != 2) {
            throw new UnauthorizedException("Refresh token invalido");
        }
        String secretToken = arrayRefreshToken[0];
        String publicToken = arrayRefreshToken[1];
        RefreshToken token = refreshTokenPersistencePort.findByTokenId(publicToken).orElseThrow( () -> new UnauthorizedException("Refresh token invalido"));

        if(!passwordEncoderPort.matches(secretToken, token.getRefreshTokenHash(), null, null)) {
            throw new UnauthorizedException("Refresh token invalido");
        }
        if(token.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new UnauthorizedException("Refresh token invalido");
        }
        if(token.getHasRevoked()){
            throw new UnauthorizedException("Refresh token invalido");
        }
        if(token.getLastUsedAt() != null) {
            throw new UnauthorizedException("Refresh token invalido");
        }
        UserAuth userAuth = userAuthPersistencePort.findById(token.getIdUser()).orElseThrow( () -> new UnauthorizedException("Token invalido"));

        token.setLastUsedAt(LocalDateTime.now());
        refreshTokenPersistencePort.save(token);

        List<Rol> roles = rolPersistencePort.rolesPorUsuario(userAuth.getId());
        List<String> rolesString = roles.stream()
                .map(Rol::getNombre)
                .toList();

        List<AplicacionRoles> aplicacionesRoles = rolPersistencePort.aplicacionesRolesPorUsuario(userAuth.getId());
        String accessToken = tokenProviderPort.generateToken(
                userAuth, rolesString, aplicacionesRoles);
        RefreshToken newRefreshToken = tokenProviderPort.getRefreshToken(userAuth.getId());

        refreshTokenPersistencePort.save(newRefreshToken);
        return new Token(accessToken, newRefreshToken.getRefreshToken(), "Bearer");
        //return new Token(accessToken, newRefreshToken.getRefreshToken(), "Bearer", userAuth.getUsername());

    }


}

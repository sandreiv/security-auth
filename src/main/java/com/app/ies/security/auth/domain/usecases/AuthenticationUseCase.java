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

        if(roles == null || roles.isEmpty()) {
            throw new AccessDeniedException("El usuario no tiene roles asignados");
        }
        List<String> rolesString = roles.stream()
                .map(Rol::getNombre)
                .toList();

        if(!passwordEncoderPort.matches(password, user.getPassword(), String.valueOf(user.getId()), user.getIdPersona()) ) {
            throw new AccessDeniedException("Credenciales Inválidas");
        }
        List<AplicacionRoles> aplicacionesRoles = rolPersistencePort.aplicacionesRolesPorUsuario(user.getId());
        String token = tokenProviderPort.generateToken(
                user, rolesString, aplicacionesRoles);
        RefreshToken refreshToken = tokenProviderPort.getRefreshToken(user.getId());
        refreshTokenPersistencePort.save(refreshToken);

        return new Token(token, refreshToken.getRefreshToken(), "Bearer");

    }

    @Override
    public Token refreshToken(String refreshToken) {
        RefreshToken storedToken = resolveStoredToken(refreshToken);

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh token invalido");
        }

        if (Boolean.TRUE.equals(storedToken.getHasRevoked())
                || !refreshTokenPersistencePort.consume(storedToken.getId())) {
            refreshTokenPersistencePort.revokeAllByUserId(storedToken.getIdUser());
            throw new UnauthorizedException("Refresh token invalido");
        }

        UserAuth userAuth = userAuthPersistencePort.findById(storedToken.getIdUser())
                .orElseThrow(() -> new UnauthorizedException("Token invalido"));

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
    }

    @Override
    public void logout(String refreshToken) {
        RefreshToken storedToken = resolveStoredToken(refreshToken);
        refreshTokenPersistencePort.revokeAllByUserId(storedToken.getIdUser());
    }

    private RefreshToken resolveStoredToken(String refreshToken) {
        String[] arrayRefreshToken = splitRefreshToken(refreshToken);
        String secretToken = arrayRefreshToken[0];
        String publicToken = arrayRefreshToken[1];
        RefreshToken token = refreshTokenPersistencePort.findByTokenId(publicToken)
                .orElseThrow(() -> new UnauthorizedException("Refresh token invalido"));

        if (!tokenProviderPort.matchesRefreshSecret(secretToken, token.getRefreshTokenHash())) {
            throw new UnauthorizedException("Refresh token invalido");
        }
        return token;
    }

    private String[] splitRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UnauthorizedException("Refresh token invalido");
        }
        String[] parts = refreshToken.split("\\.", 2);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new UnauthorizedException("Refresh token invalido");
        }
        return parts;
    }


}

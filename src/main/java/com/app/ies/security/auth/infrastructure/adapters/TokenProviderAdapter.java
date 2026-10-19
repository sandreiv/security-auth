package com.app.ies.security.auth.infrastructure.adapters;

import com.app.ies.security.auth.domain.models.RefreshToken;
import com.app.ies.security.auth.domain.models.UserAuth;
import com.app.ies.security.auth.domain.ports.security.TokenProviderPort;
import com.app.ies.security.auth.infrastructure.security.jwt.KeysSecurityProvider;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HexFormat;
import java.util.List;
import com.app.ies.security.auth.domain.models.AplicacionRoles;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenProviderAdapter implements TokenProviderPort {

    private final KeysSecurityProvider keysSecurityProvider;

    @Value("${security.expiration}")
    private Long expirationTime;

    @Value("${security.refresh.expiration}")
    private Long expirationRefreshToken;

    @Override
    public String generateToken(UserAuth user, List<String> roles) {
        PrivateKey privateKey = keysSecurityProvider.loadPrivateKey();

        Instant now = Instant.now();
        Instant expiration = now.plusMillis(expirationTime);

        return Jwts.builder().subject(user.getUsername())
                .claim("roles", roles)
                .claim("idPersona", user.getIdPersona())
                .claim("idUsuario", user.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(privateKey)
                .issuer("http://127.0.0.1:8171")
                .compact();
    }

    @Override
    public String generateToken(UserAuth user, List<String> roles,
            List<AplicacionRoles> aplicaciones) {
        PrivateKey privateKey = keysSecurityProvider.loadPrivateKey();

        Instant now = Instant.now();
        Instant expiration = now.plusMillis(expirationTime);

        return Jwts.builder().subject(user.getUsername())
                //.claim("roles", roles)
                .claim("aplicaciones", aplicaciones)
                .claim("idPersona", user.getIdPersona())
                .claim("idUsuario", user.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(privateKey)
                .issuer("http://127.0.0.1:8171")
                .compact();
    }

    @Override
    public RefreshToken getRefreshToken(Long userId) {
        String secretUUID = UUID.randomUUID().toString();
        String publicUUID = UUID.randomUUID().toString();
        String hashSecretRefreshToken = hashRefreshSecret(secretUUID);
        String refreshToken = String.format("%s.%s", secretUUID, publicUUID);
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(expirationRefreshToken);
        LocalDateTime createdAt = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        return new RefreshToken(
                null,
                publicUUID,
                refreshToken,
                hashSecretRefreshToken,
                LocalDateTime.ofInstant(expiration, ZoneId.systemDefault()),
                createdAt,
                false,
                userId
        );
    }

    @Override
    public boolean matchesRefreshSecret(String secret, String hash) {
        if (secret == null || hash == null) {
            return false;
        }
        byte[] expected = hashRefreshSecret(secret).getBytes(StandardCharsets.UTF_8);
        byte[] actual = hash.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expected, actual);
    }

    private String hashRefreshSecret(String secret) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(secret.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}

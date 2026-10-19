package com.app.ies.security.auth.infrastructure.output.persistence.mappers;



import com.app.ies.security.auth.domain.models.RefreshToken;
import com.app.ies.security.auth.infrastructure.output.persistence.entities.RefreshTokenEntity;

import java.time.LocalDateTime;

public class RefreshTokenMapper {

    public static RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastUsedAt = refreshToken.getLastUsedAt() != null
                ? refreshToken.getLastUsedAt()
                : now;
        return RefreshTokenEntity.builder()
                .id(refreshToken.getId())
                .tokenId(refreshToken.getTokenId())
                .refreshTokenHash(refreshToken.getRefreshTokenHash())
                .expiresAt(refreshToken.getExpiresAt())
                .hasRevoked(Boolean.TRUE.equals(refreshToken.getHasRevoked()) ? "S" : "N")
                .idUser(refreshToken.getIdUser())
                .lastUsedAt(lastUsedAt)
                .registradoPor(refreshToken.getIdUser() != null
                        ? String.valueOf(refreshToken.getIdUser())
                        : "SECURITYAUTH")
                .fechaCambio(now)
                .build();
    }

    public static RefreshToken toModel(RefreshTokenEntity refreshTokenEntity) {
        return new RefreshToken(
                refreshTokenEntity.getId(),
                refreshTokenEntity.getTokenId(),
                null,
                refreshTokenEntity.getRefreshTokenHash(),
                refreshTokenEntity.getExpiresAt(),
                refreshTokenEntity.getLastUsedAt(),
                "S".equalsIgnoreCase(refreshTokenEntity.getHasRevoked()),
                refreshTokenEntity.getIdUser()
        );
    }

}

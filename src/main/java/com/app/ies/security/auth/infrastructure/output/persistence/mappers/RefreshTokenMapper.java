package com.app.ies.security.auth.infrastructure.output.persistence.mappers;



import com.app.ies.security.auth.domain.models.RefreshToken;
import com.app.ies.security.auth.infrastructure.output.persistence.entities.RefreshTokenEntity;

import java.time.LocalDateTime;

public class RefreshTokenMapper {

    public static RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        return RefreshTokenEntity.builder()
                .id(refreshToken.getId())
                .tokenId(refreshToken.getTokenId())
                .refreshTokenHash(refreshToken.getRefreshTokenHash())
                .expiresAt(refreshToken.getExpiresAt())
                .hasRevoked(refreshToken.getHasRevoked())
                .idUser(refreshToken.getIdUser())
                .lastUsedAt(refreshToken.getLastUsedAt())
                .build();
    }

    public static RefreshToken toModel(RefreshTokenEntity refreshTokenEntity) {
        return new  RefreshToken(
                refreshTokenEntity.getId(),
                refreshTokenEntity.getTokenId(), null,
                refreshTokenEntity.getRefreshTokenHash(), refreshTokenEntity.getExpiresAt(),
                refreshTokenEntity.getLastUsedAt(), refreshTokenEntity.getHasRevoked(), refreshTokenEntity.getIdUser()
        );
    }

}

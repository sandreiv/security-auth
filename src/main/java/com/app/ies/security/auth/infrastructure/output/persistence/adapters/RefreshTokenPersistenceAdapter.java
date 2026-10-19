package com.app.ies.security.auth.infrastructure.output.persistence.adapters;

import com.app.ies.security.auth.domain.models.RefreshToken;
import com.app.ies.security.auth.domain.ports.persistence.RefreshTokenPersistencePort;
import com.app.ies.security.auth.infrastructure.output.persistence.entities.RefreshTokenEntity;
import com.app.ies.security.auth.infrastructure.output.persistence.mappers.RefreshTokenMapper;
import com.app.ies.security.auth.infrastructure.output.persistence.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenPersistencePort {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void save(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public Optional<RefreshToken> findByTokenId(String tokenId) {
        return refreshTokenRepository.findByTokenId(tokenId).map(RefreshTokenMapper::toModel);
    }
}

package com.app.ies.security.auth.infrastructure.output.persistence.adapters;

import com.app.ies.security.auth.domain.models.RefreshToken;
import com.app.ies.security.auth.domain.ports.persistence.RefreshTokenPersistencePort;
import com.app.ies.security.auth.infrastructure.output.persistence.entities.RefreshTokenEntity;
import com.app.ies.security.auth.infrastructure.output.persistence.mappers.RefreshTokenMapper;
import com.app.ies.security.auth.infrastructure.output.persistence.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Override
    @Transactional
    public boolean consume(Long id) {
        return refreshTokenRepository.consume(id, LocalDateTime.now()) == 1;
    }

    @Override
    @Transactional
    public void revoke(Long id) {
        refreshTokenRepository.revoke(id, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void revokeAllByUserId(Long userId) {
        refreshTokenRepository.revokeAllByIdUser(userId, LocalDateTime.now());
    }
}

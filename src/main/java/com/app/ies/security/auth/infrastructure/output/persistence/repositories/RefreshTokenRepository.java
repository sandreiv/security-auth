package com.app.ies.security.auth.infrastructure.output.persistence.repositories;

import com.app.ies.security.auth.infrastructure.output.persistence.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByTokenId(String tokenId);

}

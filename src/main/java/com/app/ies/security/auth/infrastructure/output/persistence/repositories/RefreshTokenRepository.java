package com.app.ies.security.auth.infrastructure.output.persistence.repositories;

import com.app.ies.security.auth.infrastructure.output.persistence.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByTokenId(String tokenId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE RefreshTokenEntity r
            SET r.hasRevoked = 'S', r.lastUsedAt = :now, r.fechaCambio = :now
            WHERE r.id = :id AND r.hasRevoked = 'N'
            """)
    int consume(@Param("id") Long id, @Param("now") LocalDateTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE RefreshTokenEntity r
            SET r.hasRevoked = 'S', r.fechaCambio = :now
            WHERE r.idUser = :idUser AND r.hasRevoked = 'N'
            """)
    int revokeAllByIdUser(@Param("idUser") Long idUser, @Param("now") LocalDateTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE RefreshTokenEntity r
            SET r.hasRevoked = 'S', r.fechaCambio = :now
            WHERE r.id = :id AND r.hasRevoked = 'N'
            """)
    int revoke(@Param("id") Long id, @Param("now") LocalDateTime now);

}

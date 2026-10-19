package com.app.ies.security.auth.infrastructure.output.persistence.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(schema = "vortal", name = "refreshtoken")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reto_id")
    private Long id;

    @Column(name = "reto_tokenid", unique = true, nullable = false, length = 250)
    private String tokenId;

    @Column(name = "reto_hash", nullable = false, length = 500)
    private String refreshTokenHash;

    @Column(name = "reto_expiracion", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "reto_ultimouso", nullable = false)
    private LocalDateTime lastUsedAt;

    @Column(name = "reto_revocado", nullable = false, length = 1)
    private String hasRevoked;

    @Column(name = "reto_usuarioid", nullable = false)
    private Long idUser;

    @Column(name = "reto_registradopor", nullable = false, length = 250)
    private String registradoPor;

    @Column(name = "reto_fechacambio", nullable = false)
    private LocalDateTime fechaCambio;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (hasRevoked == null) {
            hasRevoked = "N";
        }
        if (lastUsedAt == null) {
            lastUsedAt = now;
        }
        if (fechaCambio == null) {
            fechaCambio = now;
        }
        if (registradoPor == null) {
            registradoPor = "SECURITYAUTH";
        }
    }

}

package com.app.ies.security.auth.infrastructure.output.persistence.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tokenId;

    private String refreshTokenHash;
    private LocalDateTime expiresAt;
    private LocalDateTime lastUsedAt;
    private Boolean hasRevoked;
    private Long idUser;

    @PrePersist
    public void prePersist() {
        if(hasRevoked == null) {
            hasRevoked = false;
        }
    }

}



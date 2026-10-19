package com.app.ies.security.auth.domain.models;

import java.time.LocalDateTime;

public class RefreshToken {

    private Long id;
    private String tokenId;
    private String refreshToken;
    private String refreshTokenHash;
    private LocalDateTime expiresAt;
    private LocalDateTime lastUsedAt;
    private Boolean hasRevoked;
    private Long idUser;

    public RefreshToken() {
    }

    public RefreshToken(Long id, String tokenId, String refreshToken, String refreshTokenHash, LocalDateTime expiresAt, LocalDateTime lastUsedAt, Boolean hasRevoked, Long idUser) {
        this.id = id;
        this.tokenId = tokenId;
        this.refreshToken = refreshToken;
        this.refreshTokenHash = refreshTokenHash;
        this.expiresAt = expiresAt;
        this.lastUsedAt = lastUsedAt;
        this.hasRevoked = hasRevoked;
        this.idUser = idUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getRefreshTokenHash() {
        return refreshTokenHash;
    }

    public void setRefreshTokenHash(String refreshTokenHash) {
        this.refreshTokenHash = refreshTokenHash;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public Boolean getHasRevoked() {
        return hasRevoked;
    }

    public void setHasRevoked(Boolean hasRevoked) {
        this.hasRevoked = hasRevoked;
    }
}

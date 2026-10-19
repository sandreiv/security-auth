package com.app.ies.security.auth.domain.ports.persistence;


import com.app.ies.security.auth.domain.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenPersistencePort {

    void save(RefreshToken refreshToken);

    Optional<RefreshToken> findByTokenId(String tokenId);

}

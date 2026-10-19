package com.app.ies.security.auth.domain.ports.persistence;


import com.app.ies.security.auth.domain.models.UserAuth;

import java.util.Optional;

public interface UserAuthPersistencePort {
    Optional<UserAuth> findByUsername(String username);
    Optional<UserAuth> findById(Long id);
}

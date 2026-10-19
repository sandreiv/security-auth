package com.app.ies.security.auth.infrastructure.output.persistence.adapters;

import com.app.ies.security.auth.domain.models.UserAuth;
import com.app.ies.security.auth.domain.ports.persistence.UserAuthPersistencePort;
import com.app.ies.security.auth.infrastructure.output.persistence.mappers.UserEntityMapper;
import com.app.ies.security.auth.infrastructure.output.persistence.repositories.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAuthPersistenceAdapter implements UserAuthPersistencePort {
    private final UserAuthRepository userAuthRepository;
    @Override
    public Optional<UserAuth> findByUsername(String username) {
        return userAuthRepository.findByUsuario(username).map(UserEntityMapper::toUserAuth);
    }

    @Override
    public Optional<UserAuth> findById(Long id) {
        return userAuthRepository.findById(id).map(UserEntityMapper::toUserAuth);
    }
}

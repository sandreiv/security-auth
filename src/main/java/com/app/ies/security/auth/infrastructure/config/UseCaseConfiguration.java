package com.app.ies.security.auth.infrastructure.config;

import com.app.ies.security.auth.domain.api.AuthenticationServicePort;
import com.app.ies.security.auth.domain.api.FuncionalidadServicePort;
import com.app.ies.security.auth.domain.ports.persistence.FuncionalidadPersistencePort;
import com.app.ies.security.auth.domain.ports.persistence.RefreshTokenPersistencePort;
import com.app.ies.security.auth.domain.ports.persistence.RolPersistencePort;
import com.app.ies.security.auth.domain.ports.persistence.UserAuthPersistencePort;
import com.app.ies.security.auth.domain.ports.security.PasswordEncoderPort;
import com.app.ies.security.auth.domain.ports.security.TokenProviderPort;
import com.app.ies.security.auth.domain.usecases.AuthenticationUseCase;
import com.app.ies.security.auth.domain.usecases.PermisosFuncionalidadesPorRolUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCaseConfiguration {

    private final UserAuthPersistencePort userAuthPersistencePort;
    private final TokenProviderPort tokenProviderPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final RefreshTokenPersistencePort refreshTokenPersistencePort;
    private final RolPersistencePort rolPersistencePort;

    private final FuncionalidadPersistencePort funcionalidadPersistencePort;

    @Bean
    public AuthenticationServicePort authenticationServicePort() {
        return new AuthenticationUseCase(userAuthPersistencePort, passwordEncoderPort, tokenProviderPort, refreshTokenPersistencePort, rolPersistencePort);
    }

    @Bean
    FuncionalidadServicePort funcionalidadServicePort(){
        return new PermisosFuncionalidadesPorRolUseCase(funcionalidadPersistencePort);
    }

}

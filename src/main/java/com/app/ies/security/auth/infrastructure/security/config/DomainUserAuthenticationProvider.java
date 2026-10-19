package com.app.ies.security.auth.infrastructure.security.config;

import com.app.ies.security.auth.domain.models.UserAuth;
import com.app.ies.security.auth.domain.ports.persistence.UserAuthPersistencePort;
import com.app.ies.security.auth.domain.ports.security.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Proveedor de autenticación que valida las credenciales del usuario en el
 * formulario de login del Authorization Server (flujo Authorization Code).
 *
 * <p>Existe porque el Authorization Server necesita autenticar al usuario en
 * {@code /oauth2/authorize} antes de emitir un código. No podemos usar el
 * {@code DaoAuthenticationProvider} estándar porque el sistema tiene contraseñas
 * legacy cuyo {@code PasswordEncoderPort#matches} requiere el {@code idUsuario} y
 * el {@code idPersona} (esquema PBE antiguo), datos que el flujo estándar de
 * {@code UserDetails} no propaga. Este proveedor tiende ese puente reutilizando
 * la persistencia y el validador de contraseñas ya existentes en el dominio.</p>
 */
@Component
@RequiredArgsConstructor
public class DomainUserAuthenticationProvider implements AuthenticationProvider {

    private final UserAuthPersistencePort userAuthPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    /**
     * Valida usuario y contraseña contra la base de datos del dominio.
     *
     * <p>Pasos: (1) toma username y contraseña en claro del formulario;
     * (2) busca el usuario por username; (3) delega la comparación de la
     * contraseña al {@code PasswordEncoderPort}, entregándole el id de usuario y
     * el idPersona para soportar tanto BCrypt como el hash legacy; (4) si todo
     * coincide, devuelve un token autenticado cuyo nombre es el username (clave
     * que luego usará el personalizador de tokens para inyectar los claims).</p>
     *
     * @param authentication credenciales sin autenticar provenientes del form login
     * @return autenticación válida con el username como principal
     * @throws AuthenticationException si el usuario no existe o la contraseña no coincide
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = String.valueOf(authentication.getCredentials());

        UserAuth user = userAuthPersistencePort.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        boolean matches = passwordEncoderPort.matches(
                rawPassword,
                user.getPassword(),
                String.valueOf(user.getId()),
                user.getIdPersona());

        if (!matches) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        // Autoridades mínimas para la sesión del AS. La autorización fina por
        // aplicación/rol se materializa en los claims del token (ver el
        // OAuth2TokenCustomizer), no en la sesión de login.
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // El principal se modela como un User de Spring Security cuyo getName()
        // devuelve el username: ese es el enlace que el customizer usa después.
        User principal = new User(user.getUsername(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    /**
     * Indica a Spring Security que este proveedor atiende autenticaciones de
     * tipo usuario/contraseña (las que produce el formulario de login).
     *
     * @param authentication tipo de token de autenticación candidato
     * @return true si es un {@code UsernamePasswordAuthenticationToken}
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

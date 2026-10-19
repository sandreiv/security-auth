package com.app.ies.security.auth.infrastructure.security.config;

import com.app.ies.security.auth.domain.models.AplicacionRoles;
import com.app.ies.security.auth.domain.ports.persistence.RolPersistencePort;
import com.app.ies.security.auth.domain.ports.persistence.UserAuthPersistencePort;
import com.app.ies.security.auth.infrastructure.security.jwt.KeysSecurityProvider;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Configuración del OAuth2 Authorization Server (Fase 1 de la migración a OAuth2).
 *
 * <p>Levanta los endpoints estándar OAuth2/OIDC ({@code /oauth2/authorize},
 * {@code /oauth2/token}, {@code /oauth2/jwks}, {@code /.well-known/...}) reutilizando
 * la MISMA pareja de llaves RSA que ya usa el sistema. De este modo los tokens que
 * emita el Authorization Server pueden ser verificados por los Resource Servers
 * (p. ej. BTAA) exactamente igual que hoy, permitiendo que el flujo nuevo conviva
 * con el login artesanal actual ({@code /auth/login}).</p>
 *
 * <p>En esta fase el registro de clientes es en memoria; en una fase posterior se
 * migrará a persistencia JDBC.</p>
 */
@Configuration
public class AuthorizationServerConfig {

    /** Emisor (claim {@code iss}) que anunciará el Authorization Server. */
    @Value("${security.oauth2.issuer:http://localhost:9000}")
    private String issuer;

    /**
     * Cadena de filtros dedicada EXCLUSIVAMENTE a los endpoints del Authorization
     * Server. Tiene {@code @Order(1)} para interceptarse antes que la cadena de la
     * API/Resource Server (que queda en {@code @Order(2)}).
     *
     * <p>Qué hace: (1) aplica la configuración por defecto del Authorization Server
     * y limita esta cadena a sus propios endpoints mediante {@code securityMatcher};
     * (2) habilita OpenID Connect (necesario para SSO e {@code id_token});
     * (3) si un navegador (petición HTML) llega sin sesión, lo redirige a
     * {@code /login} para autenticarse; (4) habilita Resource Server JWT para que el
     * endpoint OIDC {@code /userinfo} acepte el access token.</p>
     *
     * @param http constructor de seguridad de Spring
     * @return la cadena de filtros del Authorization Server
     * @throws Exception si la configuración de {@code HttpSecurity} falla
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .oauth2AuthorizationServer(authorizationServer -> {
                    // Limita esta cadena EXCLUSIVAMENTE a los endpoints del Authorization Server.
                    http.securityMatcher(authorizationServer.getEndpointsMatcher());
                    // Habilita OpenID Connect 1.0 (id_token, /userinfo, SSO).
                    authorizationServer.oidc(Customizer.withDefaults());
                })
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // Permite que el endpoint OIDC /userinfo acepte el access token JWT.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * Repositorio de clientes registrados (en memoria en esta fase).
     *
     * <p>Registra a Vortal como cliente PÚBLICO (SPA): sin secreto, autenticación
     * de cliente {@code NONE} y PKCE obligatorio, que es el patrón seguro para
     * aplicaciones de navegador. Se le habilitan los flujos Authorization Code y
     * Refresh Token, sus URIs de redirección de desarrollo y los scopes básicos
     * OIDC más los de negocio de BTAA.</p>
     *
     * @return repositorio con el cliente Vortal registrado
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient vortalPortal = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("vortal-portal")
                // Cliente público (SPA): no usa secreto, se protege con PKCE.
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // URIs de retorno del navegador tras autenticarse (desarrollo).
                .redirectUri("http://localhost:4200/callback")
                .redirectUri("http://127.0.0.1:4200/callback")
                .postLogoutRedirectUri("http://localhost:4200")
                // Scopes: OIDC estándar + permisos de negocio (audiencia BTAA).
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("btaa.read")
                .scope("btaa.write")
                // PKCE obligatorio y sin pantalla de consentimiento en desarrollo.
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(true)
                        .requireAuthorizationConsent(false)
                        .build())
                // Vida corta del access token (revocación efectiva por expiración)
                // y rotación del refresh token en cada uso.
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(15))
                        .refreshTokenTimeToLive(Duration.ofHours(8))
                        .reuseRefreshTokens(false)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(vortalPortal);
    }

    /**
     * Fuente de llaves (JWKS) que el Authorization Server usa para FIRMAR los
     * tokens. Reutiliza la pareja RSA existente ({@code KeysSecurityProvider}) para
     * que los Resource Servers actuales sigan validando la firma sin cambios.
     *
     * <p>Se expone además, en formato JWKS, a través del endpoint
     * {@code /oauth2/jwks}, lo que en fases siguientes permitirá a los Resource
     * Servers descargar la llave pública automáticamente (y rotarla) en lugar de
     * copiar el PEM a mano.</p>
     *
     * @param keysSecurityProvider proveedor de las llaves RSA ya configuradas
     * @return fuente JWKS inmutable con la llave RSA pública y privada
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(KeysSecurityProvider keysSecurityProvider) {
        RSAPublicKey publicKey = keysSecurityProvider.loadPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) keysSecurityProvider.loadPrivateKey();

        // keyID estable para que el JWKS publicado no cambie entre reinicios.
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("securityauth-rsa-key")
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * Ajustes globales del Authorization Server. Fija el {@code issuer} (claim
     * {@code iss}) que anunciarán los tokens y el documento de descubrimiento OIDC.
     *
     * @return ajustes del Authorization Server
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(issuer)
                .build();
    }

    /**
     * Personalizador de tokens que preserva la compatibilidad con los Resource
     * Servers actuales.
     *
     * <p>El Authorization Server, por defecto, no conoce el modelo de negocio de
     * roles por aplicación. Este customizer inyecta en el ACCESS TOKEN los mismos
     * claims que hoy espera BTAA: {@code idPersona} y {@code aplicaciones} (lista de
     * objetos {@code {id, roles[]}}). Así, el converter del Resource Server sigue
     * funcionando sin cambios durante la coexistencia.</p>
     *
     * <p>Solo actúa sobre el access token y solo si el principal corresponde a un
     * usuario existente (no aplica a flujos sin usuario).</p>
     *
     * @param userAuthPersistencePort acceso a los datos del usuario autenticado
     * @param rolPersistencePort       resolución de aplicaciones y roles del usuario
     * @return customizer que enriquece el access token con los claims de negocio
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(
            UserAuthPersistencePort userAuthPersistencePort,
            RolPersistencePort rolPersistencePort) {
        return context -> {
            // Solo enriquecemos el access token (no el id_token ni el refresh).
            if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                return;
            }

            String username = context.getPrincipal().getName();
            userAuthPersistencePort.findByUsername(username).ifPresent(user -> {
                // Claim idPersona: igual que en el flujo artesanal actual.
                context.getClaims().claim("idPersona", user.getIdPersona());

                // Claim aplicaciones: lista de {id, roles[]} por aplicación, con la
                // MISMA forma JSON que consume el converter del Resource Server.
                List<AplicacionRoles> apps = rolPersistencePort.aplicacionesRolesPorUsuario(user.getId());
                List<Map<String, Object>> aplicaciones = apps.stream()
                        .map(app -> {
                            Map<String, Object> item = new HashMap<>();
                            item.put("id", app.getId());
                            item.put("roles", app.getRoles());
                            return item;
                        })
                        .collect(Collectors.toList());
                context.getClaims().claim("aplicaciones", aplicaciones);
            });
        };
    }
}

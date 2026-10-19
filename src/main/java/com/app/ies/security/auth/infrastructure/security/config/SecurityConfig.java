package com.app.ies.security.auth.infrastructure.security.config;


import com.app.ies.security.auth.infrastructure.security.jwt.KeysSecurityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.app.ies.security.auth.domain.models.AplicacionRoles;
import com.app.ies.security.auth.infrastructure.adapters.PermisoRolService;
import org.springframework.security.core.GrantedAuthority;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final PermisoRolService permisoRolService;

    /*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

    // Único bean de tipo PasswordEncoder de la aplicación. El Authorization Server
    // resuelve el PasswordEncoder con un helper interno (getOptionalBean) que exige
    // EXACTAMENTE un bean del tipo e ignora @Primary; por eso no puede haber otro
    // bean PasswordEncoder en el contexto. La validación de contraseñas de usuario
    // legacy se hace por el PasswordEncoderPort (ver DomainUserAuthenticationProvider),
    // no por este bean. En Fase 1 el cliente es público (PKCE, sin secreto).
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cadena de filtros de la API y del Resource Server (todo lo que NO son los
     * endpoints del Authorization Server).
     *
     * <p>Tiene {@code @Order(2)} para ejecutarse después de la cadena del
     * Authorization Server ({@code @Order(1)}). Además de la validación de JWT como
     * Resource Server (flujo actual), ahora habilita el {@code formLogin} con la
     * página de login por defecto: es la pantalla donde el usuario se autentica
     * durante el flujo Authorization Code, respaldada por el
     * {@link DomainUserAuthenticationProvider} (que soporta contraseñas legacy).</p>
     *
     * @param http         constructor de seguridad de Spring
     * @param authProvider proveedor de autenticación de usuarios del dominio
     * @return la cadena de filtros de la API/Resource Server
     * @throws Exception si la configuración de {@code HttpSecurity} falla
     */
    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http, DomainUserAuthenticationProvider authProvider) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( requests ->
                    requests.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
                            .requestMatchers(HttpMethod.POST, "/auth/logout").permitAll()
                            .requestMatchers(HttpMethod.GET, "/funcionalidad/funcionalidadesPorRolAplicacion").permitAll()
                            // La página de login (usada por el flujo Authorization Code) es pública.
                            .requestMatchers("/login", "/error").permitAll()
                            .anyRequest().authenticated()
                )
                .oauth2ResourceServer( oauth2 ->
                    oauth2.jwt( jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesConverter()) )
                            .authenticationEntryPoint(authenticationEntryPoint)
                            .accessDeniedHandler(accessDeniedHandler)
                )
                // Proveedor que valida credenciales contra la BD del dominio.
                .authenticationProvider(authProvider)
                .httpBasic(AbstractHttpConfigurer::disable)
                // Página de login por defecto para el flujo Authorization Code del AS.
                .formLogin(Customizer.withDefaults())
                .build();

    }


    @Bean
    public JwtDecoder jwtDecoder(KeysSecurityProvider keysSecurityProvider) {
        return NimbusJwtDecoder.withPublicKey(keysSecurityProvider.loadPublicKey()).build();
    }

    /**
     * MODIFICADO*********
     * Convierte el JWT en un token de autenticación de Spring Security, extrayendo los roles y permisos del claim "aplicaciones" y "roles".
     */
    private Converter<Jwt, AbstractAuthenticationToken> grantedAuthoritiesConverter() {
        return jwt -> {
            // 
            Object aplicacionesClaim = jwt.getClaims().get("aplicaciones");
            System.out.println("======>[DEBUG] aplicacionesClaim=" + aplicacionesClaim);
            List<GrantedAuthority> authorities = new ArrayList<>();


            if (aplicacionesClaim instanceof List) {
                List<?> raw = (List<?>) aplicacionesClaim;
                List<AplicacionRoles> apps = raw.stream().map(obj -> {
                    if (obj instanceof Map) {
                        Map<?,?> m = (Map<?,?>) obj;
                        Object idObj = m.get("id");
                        Long id = null;
                        if (idObj instanceof Number) id = ((Number) idObj).longValue();
                        @SuppressWarnings("unchecked")
                        List<String> roles = (List<String>) m.get("roles");
                        System.out.println("======>[DEBUG] AplicacionRoles: id=" + id + " roles=" + roles);
                        return new AplicacionRoles(id, roles);
                    }
                    return null;
                }).filter(a -> a != null).collect(Collectors.toList());

                Set<String> permisos = permisoRolService.permisosPorAplicaciones(apps);
                System.out.println("======>[DEBUG] Permisos TOTALES: " + permisos.size());
                permisos.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
            }

            // YA NO SE TIENE EN CUENTA EL CLAIM ROLES. SOLO APLICACIONES
            /*List<String> roles = jwt.getClaimAsStringList("roles");
            System.out.println("======>[DEBUG] Roles=" + roles);
            if (roles != null) {
                roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r)));
            }*/

            return new JwtAuthenticationToken(jwt, authorities);
        };

    }


}
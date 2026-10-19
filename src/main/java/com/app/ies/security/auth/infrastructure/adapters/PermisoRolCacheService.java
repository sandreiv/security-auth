package com.app.ies.security.auth.infrastructure.adapters;

import com.app.ies.security.auth.domain.models.Funcionalidad;
import com.app.ies.security.auth.domain.ports.persistence.FuncionalidadPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermisoRolCacheService {

    private final CacheManager cacheManager;
    private final FuncionalidadPersistencePort funcionalidadPersistencePort;


    // por modificar: dentro de las funcs no siempre se tiene esa equivalencia entre metodo y verbo  http
    private static final Map<String, String> METODOS_HTTP = Map.of(
            "LISTAR", "GET",
            "ACTUALIZAR", "PUT",
            "ELIMINAR", "DELETE",
            "GUARDAR", "POST"
    );

    /**
     * Guarda en caché los permisos de una aplicación para un conjunto de roles. 
     * La clave de caché se construye a partir del ID de la aplicación y los roles, normalizados y ordenados.
     */
    @Cacheable(value = "permissions", key = "T(com.app.ies.security.auth.infrastructure.adapters.PermisoRolCacheService).buildCacheKey(#idAplicacion, #roles)", unless = "#result == null")
    public Set<String> permisosPorAplicacion(Long idAplicacion, List<String> roles) {
        
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }

        String cacheKey = buildCacheKey(idAplicacion, roles);
        Cache cache = cacheManager.getCache("permissions");
        Object cachedValue = cache != null ? cache.get(cacheKey, Object.class) : null;
        System.out.println("======>[DEBUG] Revision CACHE permisosPorAplicacion key=" + cacheKey + " cache:" + cache + " hit=" + (cachedValue != null));

        List<Funcionalidad> funcionalidades = funcionalidadPersistencePort.funcionalidadesPorRoles(roles, idAplicacion);
        if (funcionalidades == null) {
            return Collections.emptySet();
        }

        Set<String> result = transformarPermisos(funcionalidades);
        System.out.println("======>[DEBUG] CACHEABLE permisosPorAplicacion idAplicacion=" + idAplicacion + " roles=" + roles + " result=" + result.size());
        return result;
    }

    /**
     * Guarda en caché los permisos de una aplicación para un rol específico.
     * La clave de caché se construye a partir del rol y el ID de la aplicación.
     */
    @Cacheable(value = "permissions", key = "#rol + ':' + #idAplicacion", unless = "#result == null")
    public Set<String> permisosPorRolYAplicacion(String rol, Long idAplicacion) {
        String cacheKey = rol + ":" + idAplicacion;
        Cache cache = cacheManager.getCache("permissions");
        Object cachedValue = cache != null ? cache.get(cacheKey, Object.class) : null;
        System.out.println("======>[DEBUG] CACHE permisosPorRolYAplicacion key=" + cacheKey + " Permisos en cache=" + cache);

        List<Funcionalidad> funcionalidades = funcionalidadPersistencePort.funcionalidadesPorRolEIdAplicacion(rol, idAplicacion);
        if (funcionalidades == null) {
            return Collections.emptySet();
        }

        Set<String> result = transformarPermisos(funcionalidades);
        System.out.println("======>[DEBUG] CACHEABLE permisosPorRolYAplicacion rol=" + rol + " idAplicacion=" + idAplicacion + " result=" + result.size());
        return result;
    }

    /**
     * Convierte una lista de funcionalidades en un conjunto de permisos en formato "HTTP_METHOD:URL_RECURSO".
     */
    private Set<String> transformarPermisos(List<Funcionalidad> funcionalidades) {
        return funcionalidades.stream()
                .filter(f -> f.getUrlRecurso() != null)
                .map(f -> {
                    String metodo = f.getMetodo() != null ? f.getMetodo().toUpperCase() : "*";
                    String http = METODOS_HTTP.getOrDefault(metodo, "*"); // por modificar***
                    return http + ":" + f.getUrlRecurso();
                })
                .collect(Collectors.toSet());
    }

    /**
     * Construye una clave de caché única a partir del ID de la aplicación y la lista de roles.
     */
    public static String buildCacheKey(Long idAplicacion, List<String> roles) {
        return (idAplicacion == null ? "null" : idAplicacion.toString()) + ":" + normalizeRoles(roles);
    }

    /**
     * Normaliza la lista de roles convirtiéndolos a mayúsculas, eliminando espacios y 
     * ordenándolos alfabéticamente para garantizar que la clave de caché sea consistente independientemente del orden o formato de los roles proporcionados.
     */
    private static String normalizeRoles(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return roles.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .sorted()
                .collect(Collectors.joining(","));
    }
}

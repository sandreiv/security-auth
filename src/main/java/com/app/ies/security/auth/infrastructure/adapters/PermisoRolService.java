package com.app.ies.security.auth.infrastructure.adapters;

import com.app.ies.security.auth.domain.models.AplicacionRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PermisoRolService {

    private final PermisoRolCacheService permisoRolCacheService;

    public Set<String> permisosPorRolYAplicacion(String rol, Long idAplicacion) {
        return permisoRolCacheService.permisosPorRolYAplicacion(rol, idAplicacion);
    }

    public Set<String> permisosPorAplicacion(Long idAplicacion, List<String> roles) {
        return permisoRolCacheService.permisosPorAplicacion(idAplicacion, roles);
    }

    public Set<String> permisosPorAplicaciones(List<AplicacionRoles> aplicaciones) {
        Set<String> result = new HashSet<>();
        if (aplicaciones == null) return result;
        for (AplicacionRoles a : aplicaciones) {
            result.addAll(permisosPorAplicacion(a.getId(), a.getRoles()));
        }
        return result;
    }

}

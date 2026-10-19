package com.app.ies.security.auth.infrastructure.output.persistence.adapters;

import com.app.ies.security.auth.domain.models.Rol;
import com.app.ies.security.auth.domain.ports.persistence.RolPersistencePort;
import com.app.ies.security.auth.infrastructure.output.persistence.entities.RolEntity;
import com.app.ies.security.auth.infrastructure.output.persistence.mappers.RolEntityMapper;
import com.app.ies.security.auth.infrastructure.output.persistence.repositories.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.app.ies.security.auth.domain.models.AplicacionRoles;

@Component
@RequiredArgsConstructor
public class RolPersistenceAdapter implements RolPersistencePort {

    private final RolRepository rolRepository;
    private final RolEntityMapper rolEntityMapper;

    @Override
    public List<Rol> rolesPorUsuario(Long idUsuario) {
        List<RolEntity> rolesEntity = rolRepository.rolesPorUsuario(idUsuario);
        List<Rol> rolesUsuario = rolEntityMapper.entityListToModelList(rolesEntity);
        return rolesUsuario;
    }

    @Override
    public List<AplicacionRoles> aplicacionesRolesPorUsuario(Long idUsuario) {
        List<Object[]> rows = rolRepository.aplicacionesRolesPorUsuario(idUsuario);
        List<AplicacionRoles> result = new ArrayList<>();
        for (Object[] row : rows) {
            Long id = null;
            if (row[0] instanceof Number) {
                id = ((Number) row[0]).longValue();
            }
            String joined = row[1] != null ? row[1].toString() : "";
            List<String> roles = new ArrayList<>();
            if (!joined.isEmpty()) {
                roles = Arrays.stream(joined.split(","))
                        .map(String::trim)
                        .toList();
            }
            result.add(new AplicacionRoles(id, roles));
        }
        return result;
    }
}

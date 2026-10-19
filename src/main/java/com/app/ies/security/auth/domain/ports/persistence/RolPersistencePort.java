package com.app.ies.security.auth.domain.ports.persistence;

import com.app.ies.security.auth.domain.models.Rol;
import com.app.ies.security.auth.domain.models.AplicacionRoles;

import java.util.List;

public interface RolPersistencePort {

    List<Rol> rolesPorUsuario(Long idUsuario);

    List<AplicacionRoles> aplicacionesRolesPorUsuario(Long idUsuario);

}

package com.app.ies.security.auth.domain.ports.persistence;

import com.app.ies.security.auth.domain.models.Funcionalidad;

import java.util.List;

public interface FuncionalidadPersistencePort {

    List<Funcionalidad> funcionalidadesPorRolEIdAplicacion(String rol, Long idAplicacion);
    List<Funcionalidad> funcionalidadesPorRolEIdAplicacionFrontend(String rol, Long idAplicacion);
    List<Funcionalidad> funcionalidadesPorRoles(List<String> roles, Long idAplicacion);
    List<String> funcionalidadesHijasPorIdFunPadre(Long idFunPadre);

}

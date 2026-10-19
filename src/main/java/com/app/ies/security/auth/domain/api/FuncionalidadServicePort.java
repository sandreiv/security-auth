package com.app.ies.security.auth.domain.api;

import com.app.ies.security.auth.domain.models.Funcionalidad;
import com.app.ies.security.auth.domain.models.FuncionalidadArbol;

import java.util.List;

public interface FuncionalidadServicePort {

    List<Funcionalidad> funcionalidadesPorRolEIdAplicacion(String rol, Long idAplicacion);
    List<FuncionalidadArbol> funcionalidadesPorRolEIdAplicacionFrontend(String rol, Long idAplicacion);
    List<FuncionalidadArbol> funcionalidadesPorRoles(List<String> roles, Long idAplicacion);
    List<String> funcionalidadesHijasPorIdFunPadre(Long idFunPadre);

}

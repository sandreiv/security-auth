package com.app.ies.security.auth.application.handlers.interfaces;

import com.app.ies.security.auth.application.dtos.funcionalidad.FuncionalidadArbolResponse;
import com.app.ies.security.auth.application.dtos.funcionalidad.FuncionalidadResponse;

import java.util.List;

public interface FuncionalidadHandler {

    List<FuncionalidadResponse> funcionalidadesPorRolEIdAplicacion(String rol, Long idAplicacion);
    List<FuncionalidadArbolResponse> funcionalidadesPorRolEIdAplicacionFrontend(String rol, Long idAplicacion);
    List<FuncionalidadArbolResponse> funcionalidadesPorRoles(List<String> roles, Long idAplicacion);
    List<String> funcionalidadesHijasPorIdFunPadre(Long idFunPadre);

}

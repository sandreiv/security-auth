package com.app.ies.security.auth.application.handlers;

import com.app.ies.security.auth.application.dtos.funcionalidad.FuncionalidadArbolResponse;
import com.app.ies.security.auth.application.dtos.funcionalidad.FuncionalidadResponse;
import com.app.ies.security.auth.application.handlers.interfaces.FuncionalidadHandler;
import com.app.ies.security.auth.application.mappers.FuncionalidadMapper;
import com.app.ies.security.auth.domain.api.FuncionalidadServicePort;
import com.app.ies.security.auth.domain.models.Funcionalidad;
import com.app.ies.security.auth.domain.models.FuncionalidadArbol;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FuncionalidadHandlerImpl implements FuncionalidadHandler {

    private final FuncionalidadServicePort funcionalidadServicePort;
    private final FuncionalidadMapper funcionalidadMapper;

    @Override
    public List<FuncionalidadResponse> funcionalidadesPorRolEIdAplicacion(String rol, Long idAplicacion) {
        List<Funcionalidad> funcionalidades = funcionalidadServicePort.funcionalidadesPorRolEIdAplicacion(rol, idAplicacion);
        return funcionalidadMapper.modelListToResponseList(funcionalidades);
    }

    @Override
    public List<FuncionalidadArbolResponse> funcionalidadesPorRolEIdAplicacionFrontend(String rol, Long idAplicacion) {
        List<FuncionalidadArbol> funcionalidades = funcionalidadServicePort.funcionalidadesPorRolEIdAplicacionFrontend(rol, idAplicacion);
        return funcionalidadMapper.modelListToResponseArbolList(funcionalidades);
    }

    @Override
    public List<FuncionalidadArbolResponse> funcionalidadesPorRoles(List<String> roles, Long idAplicacion) {
        List<FuncionalidadArbol> funcionalidades = funcionalidadServicePort.funcionalidadesPorRoles(roles, idAplicacion);
        return funcionalidadMapper.modelListToResponseArbolList(funcionalidades);
    }

    @Override
    public List<String> funcionalidadesHijasPorIdFunPadre(Long idFunPadre) {
        return funcionalidadServicePort.funcionalidadesHijasPorIdFunPadre(idFunPadre);
    }

}

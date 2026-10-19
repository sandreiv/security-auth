package com.app.ies.security.auth.infrastructure.output.persistence.adapters;

import com.app.ies.security.auth.domain.models.Funcionalidad;
import com.app.ies.security.auth.domain.ports.persistence.FuncionalidadPersistencePort;
import com.app.ies.security.auth.infrastructure.output.persistence.entities.FuncionalidadEntity;
import com.app.ies.security.auth.infrastructure.output.persistence.mappers.FuncionalidadEntityMapper;
import com.app.ies.security.auth.infrastructure.output.persistence.repositories.FuncionalidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FuncionalidadPersistenceAdapter implements FuncionalidadPersistencePort {

    private final FuncionalidadRepository funcionalidadRepository;
    private final FuncionalidadEntityMapper funcionalidadEntityMapper;

    @Override
    public List<Funcionalidad> funcionalidadesPorRolEIdAplicacion(String rol, Long idAplicacion) {
        List<FuncionalidadEntity> entities = funcionalidadRepository.findByNombreAndIdAplicacion(rol, idAplicacion);
        return funcionalidadEntityMapper.entityListToModelList(entities);
    }

    @Override
    public List<Funcionalidad> funcionalidadesPorRolEIdAplicacionFrontend(String rol, Long idAplicacion) {
        List<FuncionalidadEntity> entities = funcionalidadRepository.funcionalidadesPorRolYAplicacion(rol, idAplicacion);
        return funcionalidadEntityMapper.entityListToModelList(entities);
    }

    @Override
    public List<Funcionalidad> funcionalidadesPorRoles(List<String> roles, Long idAplicacion) {
        List<FuncionalidadEntity> entities = funcionalidadRepository.funcionalidadesPorRolesYAplicacion(roles, idAplicacion);
        return funcionalidadEntityMapper.entityListToModelList(entities);
    }

    @Override
    public List<String> funcionalidadesHijasPorIdFunPadre(Long idFunPadre) {
        return funcionalidadRepository.funcionalidadesHijasPorIdFunPadre(idFunPadre);
    }

}

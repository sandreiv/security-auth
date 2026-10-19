package com.app.ies.security.auth.infrastructure.output.persistence.mappers;

import com.app.ies.security.auth.domain.models.Funcionalidad;
import com.app.ies.security.auth.infrastructure.output.persistence.entities.FuncionalidadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FuncionalidadEntityMapper {

    Funcionalidad entityToModel(FuncionalidadEntity entity);
    List<Funcionalidad> entityListToModelList(List<FuncionalidadEntity> entities);

}

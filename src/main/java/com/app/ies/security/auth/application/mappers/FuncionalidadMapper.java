package com.app.ies.security.auth.application.mappers;

import com.app.ies.security.auth.application.dtos.funcionalidad.FuncionalidadArbolResponse;
import com.app.ies.security.auth.application.dtos.funcionalidad.FuncionalidadResponse;
import com.app.ies.security.auth.domain.models.Funcionalidad;
import com.app.ies.security.auth.domain.models.FuncionalidadArbol;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FuncionalidadMapper {

    FuncionalidadResponse modelToResponse(Funcionalidad model);
    List<FuncionalidadResponse> modelListToResponseList(List<Funcionalidad> modelList);

    FuncionalidadArbolResponse modelToResponseArbol(FuncionalidadArbol model);
    List<FuncionalidadArbolResponse> modelListToResponseArbolList(List<FuncionalidadArbol> modelList);

}

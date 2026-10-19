package com.app.ies.security.auth.infrastructure.output.persistence.mappers;

import com.app.ies.security.auth.domain.models.Rol;
import com.app.ies.security.auth.infrastructure.output.persistence.entities.RolEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RolEntityMapper {

    Rol entityToModel(RolEntity rolEntity);
    List<Rol> entityListToModelList(List<RolEntity> rolEntity);

    RolEntity modelToEntity(Rol rol);

}

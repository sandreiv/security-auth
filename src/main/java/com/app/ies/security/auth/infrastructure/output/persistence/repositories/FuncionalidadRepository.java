package com.app.ies.security.auth.infrastructure.output.persistence.repositories;

import com.app.ies.security.auth.infrastructure.output.persistence.entities.FuncionalidadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionalidadRepository extends JpaRepository<FuncionalidadEntity, Long> {

    @Query(value = """
        select
        	func . *
        from
        	vortal.rol rol
        	inner join vortal.rolporlet ropo on rol.rol_id = ropo.rol_id
        	inner join vortal.porlet porl on ropo.porl_id = porl.porl_id
        	inner join vortal.aplicacion apli on porl.porl_id = apli.porl_id
        	inner join vortal.funcionalidad func on apli.apli_id = func.apli_id
        where
        	rol.rol_nombre = :rol and apli.apli_id = :idAplicacion
        """, nativeQuery = true)
    List<FuncionalidadEntity> findByNombreAndIdAplicacion(@Param("rol")String rol, @Param("idAplicacion")Long idAplicacion);

    @Query(value = """
        select
            f.*
        from
            vortal.rolporletaplicacion rpa
        inner join vortal.rolaplicacionfuncionalidad raf
                on rpa.rpap_id = raf.rpap_id and rpa.apli_id = :idAplicacion
        inner join vortal.funcionalidad f
                on raf.func_id = f.func_id and f.apli_id = :idAplicacion
        inner join vortal.rol r
                on rpa.rol_id = r.rol_id and r.rol_nombre = :rol
        order by f.func_codigo
    """, nativeQuery = true)
    List<FuncionalidadEntity> funcionalidadesPorRolYAplicacion(@Param("rol") String rol, @Param("idAplicacion") Long idAplicacion);

    @Query(value = """
        select
            distinct f.*
        from
            vortal.rolporletaplicacion rpa
        inner join vortal.rolaplicacionfuncionalidad raf
                on rpa.rpap_id = raf.rpap_id and rpa.apli_id = :idAplicacion
        inner join vortal.funcionalidad f
                on raf.func_id = f.func_id and f.apli_id = :idAplicacion
        inner join vortal.rol r
                on rpa.rol_id = r.rol_id and r.rol_nombre in (:roles)
        order by f.func_codigo
    """, nativeQuery = true)
    List<FuncionalidadEntity> funcionalidadesPorRolesYAplicacion(@Param("roles") List<String> roles, @Param("idAplicacion") Long idAplicacion);

    @Query(value = """
        SELECT f.func_codigo
        FROM vortal.funcionalidad f
        WHERE f.func_funcionalidadpadre = :idFunPadre
    """, nativeQuery = true)
    List<String> funcionalidadesHijasPorIdFunPadre(@Param("idFunPadre") Long idFunPadre);

}

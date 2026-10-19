package com.app.ies.security.auth.infrastructure.output.persistence.repositories;

import com.app.ies.security.auth.infrastructure.output.persistence.entities.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {

    @Query(value = """
        select
            rol.*
        from
            GENERAL.USUARIO USUA
        inner join VORTAL.USUARIOROL USRO on
            USRO.USUA_ID = USUA.USUA_ID
        inner join VORTAL.ROL ROL on
            ROL.ROL_ID = USRO.ROL_ID
        where
            USUA.usua_id = :idUsuario
        """, nativeQuery = true)
    List<RolEntity> rolesPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query(value = """
        SELECT
            rpa.apli_id AS id,
            STRING_AGG(DISTINCT r.rol_nombre, ',' ORDER BY r.rol_nombre) AS nombres
        FROM GENERAL.USUARIO usua
        INNER JOIN VORTAL.USUARIOROL usro ON usro.usua_id = usua.usua_id
        INNER JOIN VORTAL.ROL r ON r.rol_id = usro.rol_id
        INNER JOIN VORTAL.ROLPORLETAPLICACION rpa ON rpa.rol_id = r.rol_id
        INNER JOIN VORTAL.APLICACION apli ON rpa.apli_id = apli.apli_id
        WHERE usua.usua_id = :idUsuario
        GROUP BY rpa.apli_id
        ORDER BY rpa.apli_id
    """, nativeQuery = true)
    List<Object[]> aplicacionesRolesPorUsuario(@Param("idUsuario") Long idUsuario);

}

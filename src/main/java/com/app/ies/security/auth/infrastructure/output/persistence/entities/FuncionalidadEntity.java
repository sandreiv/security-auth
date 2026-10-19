package com.app.ies.security.auth.infrastructure.output.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(schema = "vortal", name = "funcionalidad")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FuncionalidadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "func_id")
    private Long id;

    @Column(name = "apli_id")
    private Long idAplicacion;

    @Column(name = "func_nombre")
    private String nombre;

    @Column(name = "func_descripcion")
    private String descripcion;

    @Column(name = "func_orden")
    private Integer orden;

    @Column(name = "func_urlrecurso")
    private String urlRecurso;

    @Column(name = "func_codigo")
    private String codigo;

    @Column(name = "func_tipo")
    private String tipo;

    @Column(name = "func_funcionalidadpadre")
    private Long idFuncionalidadPadre;

    @Column(name = "func_fechacambio")
    private LocalDateTime fechaCambio;

    @Column(name = "func_registradopor", length = 30)
    private String registradoPor;

}

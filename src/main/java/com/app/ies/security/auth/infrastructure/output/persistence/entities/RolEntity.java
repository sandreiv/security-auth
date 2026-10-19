package com.app.ies.security.auth.infrastructure.output.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(schema = "VORTAL", name = "rol")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long id;

    @Column(name = "rol_nombre")
    private String nombre;

    @Column(name = "rol_descripcion")
    private String descripcion;

    @Column(name = "rol_tipo")
    private String tipo;

    @Column(name = "rol_publico")
    private String publico;

    @Column(name = "rol_estado")
    private String estado;

    @Column(name = "rol_perfil")
    private String perfil;

    @Column(name = "rol_fechacambio")
    private LocalDateTime fechaCambio;

    @Column(name = "rol_registradopor", length = 30)
    private String registradoPor;

}

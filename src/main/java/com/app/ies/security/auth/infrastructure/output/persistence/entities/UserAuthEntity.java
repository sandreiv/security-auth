package com.app.ies.security.auth.infrastructure.output.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(schema = "general", name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usua_id")
    private Long id;

    @Column(name = "usua_nombre")
    private String nombre;

    @Column(name = "usua_usuario")
    private String usuario;

    @Column(name = "usua_estado", length = 1)
    private String estado;

    @Column(name = "usua_contrasena")
    private String contrasena;

    @Column(name = "usua_nick")
    private String nick;

    @Column(name = "usua_fechacambio")
    private LocalDateTime fechaCambio;

    @Column(name = "usua_registradopor", length = 30)
    private String registradoPor;

    @Column(name = "usua_preguntasecreta")
    private String preguntaSecreta;

    @Column(name = "usua_respuestapregunta")
    private String respuestaPregunta;

    @Column(name = "pege_id")
    private Long pegeId;

    @Column(name = "usua_tipo")
    private String tipo;

    @Column(name = "usua_documento", length = 30)
    private String documento;

    @Column(name = "usua_idvortal")
    private BigDecimal idVortal;

    @Column(name = "usua_fechacontrasena")
    private LocalDateTime fechaContrasena;

    @Column(name = "usua_cambiarcontrasena", length = 1)
    private String cambiarContrasena;

    @Column(name = "usua_fechaultimoingreso")
    private LocalDateTime fechaUltimoIngreso;

    @Column(name = "usua_firma", length = 2000)
    private String firma;

    @Column(name = "usua_correo", length = 300)
    private String correo;

    @Column(name = "pers_idgenerico")
    private BigDecimal persIdGenerico;

    @Column(name = "pege_idcolegios")
    private BigDecimal pegeIdColegios;

}

package com.app.ies.security.auth.application.dtos.funcionalidad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionalidadResponse {

    private String codigo;
    private String nombre;
    private String urlRecurso;
    private String metodo; //Si es 1 es un boton
    private Long idFuncionalidadPadre;

}

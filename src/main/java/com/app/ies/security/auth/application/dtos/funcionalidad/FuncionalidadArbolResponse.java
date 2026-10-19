package com.app.ies.security.auth.application.dtos.funcionalidad;

import com.app.ies.security.auth.domain.models.FuncionalidadArbol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionalidadArbolResponse {

    private Long id;
    private String codigo;
    private String nombre;
    private String urlRecurso; // nuevo
    private List<FuncionalidadArbolResponse> funHijas;

}

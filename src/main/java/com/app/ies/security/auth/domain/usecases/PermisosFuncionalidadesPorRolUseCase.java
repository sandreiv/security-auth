package com.app.ies.security.auth.domain.usecases;

import com.app.ies.security.auth.domain.api.FuncionalidadServicePort;
import com.app.ies.security.auth.domain.models.Funcionalidad;
import com.app.ies.security.auth.domain.models.FuncionalidadArbol;
import com.app.ies.security.auth.domain.ports.persistence.FuncionalidadPersistencePort;

import java.util.*;

public class PermisosFuncionalidadesPorRolUseCase implements FuncionalidadServicePort {

    private final FuncionalidadPersistencePort funcionalidadPersistencePort;

    private static final Map<String, String> METODOS_HTTP = Map.of(
            "LISTAR", "GET",
            "ACTUALIZAR", "PATCH",
            "ELIMINAR", "DELETE",
            "GUARDAR", "POST"
    );

    public PermisosFuncionalidadesPorRolUseCase(FuncionalidadPersistencePort funcionalidadPersistencePort) {
        this.funcionalidadPersistencePort = funcionalidadPersistencePort;
    }

    @Override
    public List<Funcionalidad> funcionalidadesPorRolEIdAplicacion(String rol, Long idAplicacion) {

        List<Funcionalidad> funcionalidades = funcionalidadPersistencePort.funcionalidadesPorRolEIdAplicacion(rol, idAplicacion);

        for (Funcionalidad f : funcionalidades) {
            if ("1".equals(f.getTipo()) && f.getNombre() != null) {
                String nombre = f.getNombre().trim().toUpperCase();
                f.setMetodo(METODOS_HTTP.get(nombre));
            }
        }

        /*
        System.out.println("ROL QUE LLEGA AL MICRO DE SEGURIDAD: " + rol );
        if ("ESTUDIANTE".equals(rol)) {
            System.out.println("ROL QUE LLEGA AL MICRO DE SEGURIDAD11111: " + rol );
            Funcionalidad funcionalidad = new Funcionalidad();
            funcionalidad.setUrlRecurso("/programaPad/**");
            funcionalidad.setMetodo("GET");
            funcionalidades.add(funcionalidad);
        }
         */

        System.out.println("===========>[DEBUG]FUNCIONALIDADES QUE LLEGAN AL MICRO DE SEGURIDAD: " + funcionalidades.size());

        return funcionalidades;
    }

    @Override
    public List<FuncionalidadArbol> funcionalidadesPorRolEIdAplicacionFrontend(String rol, Long idAplicacion) {

        List<Funcionalidad> funcionalidades = funcionalidadPersistencePort.funcionalidadesPorRolEIdAplicacionFrontend(rol, idAplicacion);
        List<FuncionalidadArbol> resultado = construirArbol(funcionalidades);
        ordenarArbol(resultado);
        return resultado;
    }

    @Override
    public List<FuncionalidadArbol> funcionalidadesPorRoles(List<String> roles, Long idAplicacion) {
        List<Funcionalidad> funcionalidades = funcionalidadPersistencePort.funcionalidadesPorRoles(roles, idAplicacion);
        List<FuncionalidadArbol> resultado = construirArbol(funcionalidades);
        ordenarArbol(resultado);
        return resultado;
    }

    @Override
    public List<String> funcionalidadesHijasPorIdFunPadre(Long idFunPadre) {
        return funcionalidadPersistencePort.funcionalidadesHijasPorIdFunPadre(idFunPadre);
    }

    private List<FuncionalidadArbol> construirArbol(List<Funcionalidad> lista) {

        Map<Long, FuncionalidadArbol> mapa = new LinkedHashMap<>();
        List<FuncionalidadArbol> raiz = new ArrayList<>();

        for (Funcionalidad f : lista) {

            FuncionalidadArbol nodo = new FuncionalidadArbol();
            nodo.setId(f.getId());
            nodo.setCodigo(f.getCodigo());
            nodo.setNombre(f.getNombre());
            nodo.setUrlRecurso(f.getUrlRecurso());
            nodo.setTipo(f.getTipo());
            nodo.setIdFuncionalidadPadre(f.getIdFuncionalidadPadre());
            nodo.setFunHijas(new ArrayList<>());

            mapa.put(nodo.getId(), nodo);
        }

        // 2. Relacionar
        for (FuncionalidadArbol nodo : mapa.values()) {

            if (nodo.getIdFuncionalidadPadre() == null) {
                raiz.add(nodo);
            } else {
                FuncionalidadArbol padre = mapa.get(nodo.getIdFuncionalidadPadre());
                if (padre != null) {
                    padre.getFunHijas().add(nodo);
                } else {
                    System.out.println(" ->Padre NO encontrado para: " + nodo.getId());
                }
            }
        }
        return raiz;
    }

    private void ordenarArbol(List<FuncionalidadArbol> lista) {
        lista.sort(Comparator.comparing(FuncionalidadArbol::getCodigo));
        for (FuncionalidadArbol nodo : lista) {
            if (nodo.getFunHijas() != null && !nodo.getFunHijas().isEmpty()) {
                ordenarArbol(nodo.getFunHijas());
            }
        }
    }

}

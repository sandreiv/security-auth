package com.app.ies.security.auth.infrastructure.input;

import com.app.ies.security.auth.application.dtos.funcionalidad.FuncionalidadArbolResponse;
import com.app.ies.security.auth.application.dtos.funcionalidad.FuncionalidadResponse;
import com.app.ies.security.auth.application.handlers.interfaces.FuncionalidadHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionalidad")
@RequiredArgsConstructor
public class FuncionalidadController {

    private final FuncionalidadHandler funcionalidadHandler;

    @GetMapping("/rol-aplicacion")
    public ResponseEntity<List<FuncionalidadResponse>> obtenerPorRolYAplicacion(@RequestParam String rol, @RequestParam Long idAplicacion) {
        List<FuncionalidadResponse> response = funcionalidadHandler.funcionalidadesPorRolEIdAplicacion(rol, idAplicacion);
        if (response.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }

    /*
    Endpoint para obtener el árbol de funcionalidades para un rol y una aplicación.
    @param rol: El rol para el cual se desea obtener el árbol de funcionalidades.
    @param idAplicacion: El ID de la aplicación para la cual se desea obtener el árbol de funcionalidades.
    @return ResponseEntity con la lista de funcionalidades en forma de árbol.
    */
    @GetMapping("/arbol")
    public ResponseEntity<List<FuncionalidadArbolResponse>> obtenerArbolPorRolYAplicacion(@RequestParam String rol, @RequestParam Long idAplicacion) {
        List<FuncionalidadArbolResponse> response = funcionalidadHandler.funcionalidadesPorRolEIdAplicacionFrontend(rol, idAplicacion);
        return ResponseEntity.ok(response);
    }


    /*
    Endpoint para obtener los permisos para un rol y una aplicación.
    @param rol: El rol para el cual se desea obtener los permisos.
    @param idAplicacion: El ID de la aplicación para la cual se desea obtener los permisos.
    @return ResponseEntity con la lista de permisos.
    */

    //@GetMapping("/permisos")
    //public ResponseEntity<List<String>> obtenerPermisosPorRolYAplicacion(@RequestParam String rol, @RequestParam Long idAplicacion) {
    //}

    @GetMapping("/permisos/{idFunPadre}")
    public ResponseEntity<List<String>> obtenerHijasPorIdPadre(@PathVariable Long idFunPadre) {
        List<String> response = funcionalidadHandler.funcionalidadesHijasPorIdFunPadre(idFunPadre);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obtener todas las funcionalidades en forma de árbol para una lista de roles dada.
     */
    @GetMapping("/arbol-roles")
    public ResponseEntity<List<FuncionalidadArbolResponse>> obtenerArbolPorRoles(@RequestParam List<String> roles, @RequestParam Long idAplicacion) {
        if (roles == null || roles.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<FuncionalidadArbolResponse> response = funcionalidadHandler.funcionalidadesPorRoles(roles, idAplicacion);
        return ResponseEntity.ok(response);
    }

}

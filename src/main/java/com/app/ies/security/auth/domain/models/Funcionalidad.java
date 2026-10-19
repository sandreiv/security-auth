package com.app.ies.security.auth.domain.models;

public class Funcionalidad {

    private Long id;
    private String codigo;
    private String nombre;
    private String urlRecurso;
    private String tipo;
    private String metodo; //Si es 1 es un boton
    private Long idFuncionalidadPadre;

    public Funcionalidad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlRecurso() {
        return urlRecurso;
    }

    public void setUrlRecurso(String urlRecurso) {
        this.urlRecurso = urlRecurso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public Long getIdFuncionalidadPadre() {
        return idFuncionalidadPadre;
    }

    public void setIdFuncionalidadPadre(Long idFuncionalidadPadre) {
        this.idFuncionalidadPadre = idFuncionalidadPadre;
    }
}

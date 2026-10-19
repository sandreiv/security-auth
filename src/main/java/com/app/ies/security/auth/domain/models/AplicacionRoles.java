package com.app.ies.security.auth.domain.models;

import java.util.List;

public class AplicacionRoles {

    private Long id;
    private List<String> roles;

    public AplicacionRoles() {
    }

    public AplicacionRoles(Long id, List<String> roles) {
        this.id = id;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}

package com.helpdesk.helpdesk.dto;

import com.helpdesk.helpdesk.model.Rol;
import com.helpdesk.helpdesk.model.Usuario;

public class UsuarioResponse {
    private Long id;
    private String nombres;
    private String correo;
    private Rol rol;

    public static UsuarioResponse of(Usuario u) {
        UsuarioResponse r = new UsuarioResponse();
        r.setId(u.getId());
        r.setNombres(u.getNombres());
        r.setCorreo(u.getCorreo());
        r.setRol(u.getRol());

        return r;
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}

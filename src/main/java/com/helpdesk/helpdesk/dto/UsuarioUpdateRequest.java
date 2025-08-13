package com.helpdesk.helpdesk.dto;

import com.helpdesk.helpdesk.model.Rol;

public class UsuarioUpdateRequest {
    private String nombres;
    private String correo;
    private Rol rol;

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

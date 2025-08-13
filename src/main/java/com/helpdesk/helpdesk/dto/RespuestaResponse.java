package com.helpdesk.helpdesk.dto;

import java.time.LocalDateTime;

public class RespuestaResponse {
    private Long id;
    private String autorNombre;
    private String autorRol;
    private String mensaje;
    private LocalDateTime fecha;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAutorNombre() { return autorNombre; }
    public void setAutorNombre(String autorNombre) { this.autorNombre = autorNombre; }
    public String getAutorRol() { return autorRol; }
    public void setAutorRol(String autorRol) { this.autorRol = autorRol; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}

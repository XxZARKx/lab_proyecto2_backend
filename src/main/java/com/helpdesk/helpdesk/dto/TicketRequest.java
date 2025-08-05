package com.helpdesk.helpdesk.dto;

import com.helpdesk.helpdesk.model.Prioridad;

public class TicketRequest {
    private String titulo;
    private String descripcion;
    private Prioridad prioridad;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Prioridad getPrioridad() { return prioridad; }
    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }
}

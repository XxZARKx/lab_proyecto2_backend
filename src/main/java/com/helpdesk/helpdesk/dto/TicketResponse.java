package com.helpdesk.helpdesk.dto;

import com.helpdesk.helpdesk.model.EstadoTicket;
import com.helpdesk.helpdesk.model.Prioridad;
import com.helpdesk.helpdesk.model.TipoCategoria;

import java.time.LocalDateTime;

public class TicketResponse {
    private Long id;
    private String titulo;
    private String descripcion;
    private EstadoTicket estado;
    private Prioridad prioridad;
    private LocalDateTime fechaCreacion;
    private TipoCategoria categoria;

    public TicketResponse() {}

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public EstadoTicket getEstado() { return estado; }
    public void setEstado(EstadoTicket estado) { this.estado = estado; }

    public Prioridad getPrioridad() { return prioridad; }
    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public TipoCategoria getCategoria() { return categoria; }
    public void setCategoria(TipoCategoria categoria) { this.categoria = categoria; }
}

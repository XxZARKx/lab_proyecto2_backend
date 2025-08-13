package com.helpdesk.helpdesk.dto;

import com.helpdesk.helpdesk.model.EstadoTicket;
import com.helpdesk.helpdesk.model.Prioridad;

import java.time.LocalDateTime;

public class TicketResponse {
    private Long id;
    private String titulo;
    private String descripcion;
    private EstadoTicket estado;
    private Prioridad prioridad;
    private LocalDateTime fechaCreacion;

    // Categoría embebida (solo lo que necesitamos en el front)
    public static class CategoriaDTO {
        private Long id;
        private String nombre;
        private String descripcion;

        public CategoriaDTO() {}
        public CategoriaDTO(Long id, String nombre, String descripcion) {
            this.id = id;
            this.nombre = nombre;
            this.descripcion = descripcion;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }

    private CategoriaDTO categoria; // <- nuevo

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

    public CategoriaDTO getCategoria() { return categoria; }
    public void setCategoria(CategoriaDTO categoria) { this.categoria = categoria; }
}

package com.helpdesk.helpdesk.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoTicket estado;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", length = 30)
    private TipoCategoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // quien crea el ticket

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico; // técnico asignado

    private LocalDateTime fechaCreacion;

    // CONSTRUCTORES
    public Ticket() {}

    public Ticket(String titulo, String descripcion, EstadoTicket estado, Prioridad prioridad,
                  Usuario usuario, Usuario tecnico, LocalDateTime fechaCreacion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.prioridad = prioridad;
        this.usuario = usuario;
        this.tecnico = tecnico;
        this.fechaCreacion = fechaCreacion;
    }

    // GETTERS y SETTERS
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

    public TipoCategoria getCategoria() { return categoria; }
    public void setCategoria(TipoCategoria categoria) { this.categoria = categoria; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Usuario getTecnico() { return tecnico; }

    public void setTecnico(Usuario tecnico) { this.tecnico = tecnico; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}

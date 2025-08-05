package com.helpdesk.helpdesk.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "respuestas_ticket")
public class RespuestaTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private Usuario autor;

    @Column(length = 2000)
    private String mensaje;

    private LocalDateTime fechaRespuesta;

    public RespuestaTicket() {}

    public RespuestaTicket(Ticket ticket, Usuario autor, String mensaje, LocalDateTime fechaRespuesta) {
        this.ticket = ticket;
        this.autor = autor;
        this.mensaje = mensaje;
        this.fechaRespuesta = fechaRespuesta;
    }

    public Long getId() { return id; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(LocalDateTime fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }
}

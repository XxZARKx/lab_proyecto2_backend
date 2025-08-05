package com.helpdesk.helpdesk.dto;

public class RespuestaRequest {
    private Long ticketId;
    private String mensaje;

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}

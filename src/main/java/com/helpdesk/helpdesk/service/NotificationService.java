package com.helpdesk.helpdesk.service;

import com.helpdesk.helpdesk.model.Notificacion;
import com.helpdesk.helpdesk.model.RespuestaTicket;
import com.helpdesk.helpdesk.model.Ticket;
import com.helpdesk.helpdesk.model.TipoNotificacion;
import com.helpdesk.helpdesk.model.Usuario;
import com.helpdesk.helpdesk.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Transactional
    public void notificarCambioEstado(Ticket ticket) {
        Usuario usuario = ticket.getUsuario();

        String asunto = "Actualización del estado de tu ticket #" + ticket.getId();
        String mensaje = "Hola " + usuario.getNombres() + ",\n\n"
                + "Tu ticket titulado \"" + ticket.getTitulo() + "\" ha cambiado de estado a: "
                + ticket.getEstado() + ".\n\n"
                + "Saludos,\nEquipo de Soporte Helpdesk";
        enviarCorreo(usuario.getCorreo(), asunto, mensaje);

        guardarNotificacion(usuario,
                "Estado de tu ticket actualizado",
                "El ticket #" + ticket.getId() + " cambió a " + ticket.getEstado(),
                TipoNotificacion.TICKET_ESTADO,
                ticket.getId());
    }

    @Transactional
    public void registrarNotificacionTicketAsignado(Ticket ticket) {
        if (ticket.getUsuario() == null) return;

        String asunto = "Tu ticket #" + ticket.getId() + " fue asignado";
        String mensaje = "Hola " + ticket.getUsuario().getNombres() + ",\n\n"
                + "Tu ticket \"" + ticket.getTitulo() + "\" fue asignado a un técnico.\n\n"
                + "Saludos,\nEquipo de Soporte Helpdesk";
        enviarCorreo(ticket.getUsuario().getCorreo(), asunto, mensaje);

        guardarNotificacion(ticket.getUsuario(),
                "Ticket asignado",
                "El ticket #" + ticket.getId() + " fue asignado a un técnico.",
                TipoNotificacion.TICKET_ASIGNADO,
                ticket.getId());
    }

    @Transactional
    public void registrarNotificacionRespuesta(RespuestaTicket respuesta) {
        Ticket ticket = respuesta.getTicket();
        if (ticket == null || ticket.getUsuario() == null) return;

        String asunto = "Nueva respuesta en tu ticket #" + ticket.getId();
        String mensaje = "Hola " + ticket.getUsuario().getNombres() + ",\n\n"
                + "Tu ticket \"" + ticket.getTitulo() + "\" tiene una nueva respuesta.\n\n"
                + "Saludos,\nEquipo de Soporte Helpdesk";
        enviarCorreo(ticket.getUsuario().getCorreo(), asunto, mensaje);

        guardarNotificacion(ticket.getUsuario(),
                "Nueva respuesta en tu ticket",
                "Tu ticket #" + ticket.getId() + " tiene una nueva respuesta.",
                TipoNotificacion.TICKET_RESPUESTA,
                ticket.getId());
    }

    private void enviarCorreo(String para, String asunto, String cuerpo) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(para);
            email.setSubject(asunto);
            email.setText(cuerpo);
            mailSender.send(email);
        } catch (Exception ex) {
            System.err.println("No se pudo enviar correo: " + ex.getMessage());
        }
    }

    private void guardarNotificacion(Usuario destinatario,
                                     String titulo,
                                     String mensaje,
                                     TipoNotificacion tipo,
                                     Long ticketId) {
        Notificacion n = new Notificacion();
        n.setUsuario(destinatario);
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        n.setTipo(tipo);
        n.setTicketId(ticketId);
        n.setCreadoEn(LocalDateTime.now());
        notificacionRepository.save(n);
    }
}

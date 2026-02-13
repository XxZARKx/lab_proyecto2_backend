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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Async
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

    @Async
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

    @Async
    @Transactional
    public void registrarNotificacionRespuesta(RespuestaTicket respuesta) {
        Ticket ticket = respuesta.getTicket();
        if (ticket == null) return;

        Usuario autor = respuesta.getAutor();
        Usuario destinatario;
        if (autor.getId().equals(ticket.getUsuario().getId())) {
            destinatario = ticket.getTecnico();
        } else {
            destinatario = ticket.getUsuario();
        }
        if (destinatario == null) return;

        String asunto = "Nueva respuesta en el ticket #" + ticket.getId();
        String mensaje = "Hola " + destinatario.getNombres() + ",\n\n"
                + "Hay un nuevo mensaje en el ticket \"" + ticket.getTitulo() + "\".\n\n"
                + "Mensaje:\n" + respuesta.getMensaje() + "\n\n"
                + "Saludos,\nEquipo de Soporte Helpdesk";
        enviarCorreo(destinatario.getCorreo(), asunto, mensaje);

        guardarNotificacion(destinatario,
                "Nueva respuesta en tu ticket",
                "Ticket #" + ticket.getId() + ": " + respuesta.getMensaje(),
                TipoNotificacion.TICKET_RESPUESTA,
                ticket.getId());
    }

    private void enviarCorreo(String para, String asunto, String cuerpo) {
        System.out.println(">>> SIMULACIÓN DE CORREO <<<");
        System.out.println("Para: " + para);
        System.out.println("Asunto: " + asunto);
        System.out.println("--------------------------------");
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


    @Async
    @Transactional
    public void notificarTecnicoNuevaAsignacion(Ticket ticket) {
        if (ticket.getTecnico() == null) return;

        Usuario tecnico = ticket.getTecnico();

        String asunto = "Nuevo ticket asignado: #" + ticket.getId();
        String mensaje = "Hola " + tecnico.getNombres() + ",\n\n"
                + "Se te ha asignado automáticamente un nuevo ticket.\n"
                + "Título: " + ticket.getTitulo() + "\n"
                + "Prioridad: " + ticket.getPrioridad() + "\n\n"
                + "Por favor revisa tu bandeja de entrada en la plataforma.\n\n"
                + "Saludos,\nSistema Helpdesk";

        enviarCorreo(tecnico.getCorreo(), asunto, mensaje);

        guardarNotificacion(tecnico,
                "Nuevo ticket asignado",
                "Tienes un nuevo ticket asignado: #" + ticket.getId(),
                TipoNotificacion.TICKET_ASIGNADO,
                ticket.getId());
    }
}

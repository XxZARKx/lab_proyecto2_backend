package com.helpdesk.helpdesk.service;


import com.helpdesk.helpdesk.model.Ticket;
import com.helpdesk.helpdesk.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    public void notificarCambioEstado(Ticket ticket) {
        Usuario usuario = ticket.getUsuario();

        String asunto = "Actualización del estado de tu ticket #" + ticket.getId();
        String mensaje = "Hola " + usuario.getNombres() + ",\n\n"
                + "Tu ticket titulado \"" + ticket.getTitulo() + "\" ha cambiado de estado a: "
                + ticket.getEstado() + ".\n\n"
                + "Saludos,\nEquipo de Soporte Helpdesk";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(usuario.getCorreo());
        email.setSubject(asunto);
        email.setText(mensaje);

        mailSender.send(email);
    }
}

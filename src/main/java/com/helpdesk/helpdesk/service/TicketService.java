package com.helpdesk.helpdesk.service;

import com.helpdesk.helpdesk.dto.TicketRequest;
import com.helpdesk.helpdesk.model.*;
import com.helpdesk.helpdesk.repository.CategoriaRepository;
import com.helpdesk.helpdesk.repository.RespuestaTicketRepository;
import com.helpdesk.helpdesk.repository.TicketRepository;
import com.helpdesk.helpdesk.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RespuestaTicketRepository respuestaRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Ticket crearTicket(TicketRequest request, String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Ticket ticket = new Ticket();
        ticket.setTitulo(request.getTitulo());
        ticket.setDescripcion(request.getDescripcion());

        Prioridad prioridad = request.getPrioridad() != null ? request.getPrioridad() : Prioridad.MEDIA;
        ticket.setPrioridad(prioridad);

        ticket.setEstado(EstadoTicket.PENDIENTE);
        ticket.setUsuario(usuario);
        ticket.setFechaCreacion(LocalDateTime.now());

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            ticket.setCategoria(categoria);
        } else {
            ticket.setCategoria(null);
        }

        return ticketRepository.save(ticket);
    }

    public Ticket asignarTicket(Long ticketId, Long tecnicoId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        Usuario tecnico = usuarioRepository.findById(tecnicoId)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));

        if (tecnico.getRol() != Rol.TECNICO) {
            throw new RuntimeException("El usuario no es técnico");
        }

        ticket.setTecnico(tecnico);
        ticket.setEstado(EstadoTicket.ASIGNADO);

        return ticketRepository.save(ticket);
    }

    public Ticket actualizarEstado(Long ticketId, EstadoTicket nuevoEstado) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        ticket.setEstado(nuevoEstado);
        Ticket actualizado = ticketRepository.save(ticket);
        notificationService.notificarCambioEstado(actualizado);
        return actualizado;
    }

    public List<Ticket> listarTicketsPendientes() {
        return ticketRepository.findByEstado(EstadoTicket.PENDIENTE);
    }

    public List<Ticket> consultarHistorial(Usuario usuario) {
        switch (usuario.getRol()) {
            case USUARIO:
                return ticketRepository.findByUsuarioId(usuario.getId());
            case TECNICO:
                return ticketRepository.findByTecnicoId(usuario.getId());
            case ADMINISTRADOR:
                return ticketRepository.findAll();
            default:
                throw new RuntimeException("Rol no soportado");
        }
    }

    public List<Ticket> generarReporte(EstadoTicket estado,
                                       Prioridad prioridad,
                                       Long tecnicoId,
                                       LocalDateTime fechaInicio,
                                       LocalDateTime fechaFin) {

        return ticketRepository.filtrarReporte(
                estado != null ? estado.name() : null,
                prioridad != null ? prioridad.name() : null,
                tecnicoId,
                fechaInicio != null ? fechaInicio.toString() : null,
                fechaFin != null ? fechaFin.toString() : null
        );
    }


    public RespuestaTicket responderTicket(Long ticketId, String mensaje, Usuario autor) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        RespuestaTicket respuesta = new RespuestaTicket();
        respuesta.setTicket(ticket);
        respuesta.setAutor(autor);
        respuesta.setMensaje(mensaje);
        respuesta.setFechaRespuesta(LocalDateTime.now());

        return respuestaRepository.save(respuesta);
    }
}

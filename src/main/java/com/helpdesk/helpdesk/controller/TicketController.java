package com.helpdesk.helpdesk.controller;


import com.helpdesk.helpdesk.dto.RespuestaRequest;
import com.helpdesk.helpdesk.dto.TicketRequest;
import com.helpdesk.helpdesk.model.EstadoTicket;
import com.helpdesk.helpdesk.model.Prioridad;
import com.helpdesk.helpdesk.model.RespuestaTicket;
import com.helpdesk.helpdesk.model.Ticket;
import com.helpdesk.helpdesk.security.UsuarioDetails;
import com.helpdesk.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> crearTicket(@RequestBody TicketRequest request,
                                         @AuthenticationPrincipal UsuarioDetails userDetails) {
        Ticket ticket = ticketService.crearTicket(request, userDetails.getUsuario().getCorreo());
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{id}/asignar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> asignarTicket(@PathVariable Long id,
                                           @RequestParam Long tecnicoId) {
        Ticket ticket = ticketService.asignarTicket(id, tecnicoId);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id,
                                              @RequestParam EstadoTicket estado) {
        Ticket ticket = ticketService.actualizarEstado(id, estado);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<?> listarPendientes() {
        List<Ticket> tickets = ticketService.listarTicketsPendientes();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('USUARIO', 'TECNICO', 'ADMINISTRADOR')")
    public ResponseEntity<?> consultarHistorial(@AuthenticationPrincipal UsuarioDetails userDetails) {
        List<Ticket> historial = ticketService.consultarHistorial(userDetails.getUsuario());
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/reporte")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> generarReporte(
            @RequestParam(required = false) EstadoTicket estado,
            @RequestParam(required = false) Prioridad prioridad,
            @RequestParam(required = false) Long tecnicoId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaFin
    ) {
        List<Ticket> reporte = ticketService.generarReporte(estado, prioridad, tecnicoId, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }


    @PostMapping("/responder")
    @PreAuthorize("hasAnyRole('TECNICO', 'USUARIO')")
    public ResponseEntity<?> responderTicket(@RequestBody RespuestaRequest request,
                                             @AuthenticationPrincipal UsuarioDetails userDetails) {
        RespuestaTicket respuesta = ticketService.responderTicket(
                request.getTicketId(),
                request.getMensaje(),
                userDetails.getUsuario()
        );
        return ResponseEntity.ok(respuesta);
    }
}

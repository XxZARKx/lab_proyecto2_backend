package com.helpdesk.helpdesk.controller;

import com.helpdesk.helpdesk.dto.NotificacionResponse;
import com.helpdesk.helpdesk.model.Notificacion;
import com.helpdesk.helpdesk.repository.NotificacionRepository;
import com.helpdesk.helpdesk.security.UsuarioDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
@PreAuthorize("hasAnyRole('USUARIO','TECNICO','ADMINISTRADOR')")
public class NotificacionController {

    private final NotificacionRepository repo;

    public NotificacionController(NotificacionRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<Page<NotificacionResponse>> listar(
            @AuthenticationPrincipal UsuarioDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean unread
    ) {
        Long uid = userDetails.getUsuario().getId();
        Page<Notificacion> p = unread
                ? repo.findByUsuarioIdAndLeidaEnIsNullOrderByCreadoEnDesc(uid, PageRequest.of(page, size))
                : repo.findByUsuarioIdOrderByCreadoEnDesc(uid, PageRequest.of(page, size));

        Page<NotificacionResponse> body = p.map(n -> {
            NotificacionResponse r = new NotificacionResponse();
            r.setId(n.getId());
            r.setTitulo(n.getTitulo());
            r.setMensaje(n.getMensaje());
            r.setTipo(n.getTipo());
            r.setTicketId(n.getTicketId());
            r.setCreadoEn(n.getCreadoEn());
            r.setLeida(n.getLeidaEn() != null);
            return r;
        });

        return ResponseEntity.ok(body);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> contarNoLeidas(@AuthenticationPrincipal UsuarioDetails userDetails) {
        Long uid = userDetails.getUsuario().getId();
        long count = repo.countByUsuarioIdAndLeidaEnIsNull(uid);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id,
                                            @AuthenticationPrincipal UsuarioDetails userDetails) {
        return repo.findById(id)
                .filter(n -> n.getUsuario().getId().equals(userDetails.getUsuario().getId()))
                .map(n -> {
                    if (n.getLeidaEn() == null) {
                        n.setLeidaEn(java.time.LocalDateTime.now());
                        repo.save(n);
                    }
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/leer-todas")
    @Transactional
    public ResponseEntity<Void> marcarTodasLeidas(@AuthenticationPrincipal UsuarioDetails userDetails) {
        Long uid = userDetails.getUsuario().getId();
        repo.marcarTodasComoLeidas(uid);
        return ResponseEntity.ok().build();
    }
}

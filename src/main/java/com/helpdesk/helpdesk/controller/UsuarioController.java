package com.helpdesk.helpdesk.controller;

import com.helpdesk.helpdesk.dto.UsuarioResponse;
import com.helpdesk.helpdesk.dto.UsuarioUpdateRequest;
import com.helpdesk.helpdesk.model.Usuario;
import com.helpdesk.helpdesk.repository.UsuarioRepository;
import com.helpdesk.helpdesk.security.UsuarioDetails;
import com.helpdesk.helpdesk.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService usuarioService,
                             UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(@AuthenticationPrincipal UsuarioDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUsuario());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios.stream().map(UsuarioResponse::of).toList());
    }

    @GetMapping("/tecnicos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<UsuarioResponse>> obtenerTecnicos() {
        List<Usuario> tecnicos = usuarioService.obtenerTecnicos();
        return ResponseEntity.ok(tecnicos.stream().map(UsuarioResponse::of).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> obtener(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponse::of)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody UsuarioUpdateRequest req) {
        return usuarioRepository.findById(id).map(u -> {
            if (req.getCorreo() != null &&
                    usuarioRepository.existsByCorreoIgnoreCaseAndIdNot(req.getCorreo(), id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("El correo ya está en uso");
            }

            if (req.getNombres() != null) u.setNombres(req.getNombres());
            if (req.getCorreo()  != null) u.setCorreo(req.getCorreo());
            if (req.getRol()     != null) u.setRol(req.getRol());

            usuarioRepository.save(u);
            return ResponseEntity.ok(UsuarioResponse.of(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminar(@PathVariable Long id,
                                      @AuthenticationPrincipal UsuarioDetails me) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (me != null && me.getUsuario().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No puedes eliminar tu propio usuario.");
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

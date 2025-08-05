package com.helpdesk.helpdesk.controller;

import com.helpdesk.helpdesk.dto.LoginRequest;
import com.helpdesk.helpdesk.dto.RegistroRequest;
import com.helpdesk.helpdesk.model.Usuario;
import com.helpdesk.helpdesk.security.JwtUtil;
import com.helpdesk.helpdesk.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;


    @Autowired
    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        Usuario usuario = usuarioService.registrarUsuario(request);
        return ResponseEntity.ok("Usuario registrado con ID: " + usuario.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioService.login(request);
        String token = jwtUtil.generarToken(usuario.getCorreo());
        return ResponseEntity.ok("Bearer " + token);
    }

}

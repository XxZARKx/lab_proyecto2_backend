package com.helpdesk.helpdesk.service;

import com.helpdesk.helpdesk.dto.RegistroRequest;
import com.helpdesk.helpdesk.dto.LoginRequest;
import com.helpdesk.helpdesk.model.Rol;
import com.helpdesk.helpdesk.model.Usuario;
import com.helpdesk.helpdesk.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(RegistroRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        Rol rolAsignado = request.getRol() != null ? request.getRol() : Rol.USUARIO;

        Usuario usuario = new Usuario();
        usuario.setNombres(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getContrasena()));
        usuario.setRol(rolAsignado);

        return usuarioRepository.save(usuario);
    }


    public Usuario login(LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(request.getCorreo());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(request.getContrasena(), usuario.getPassword())) {
                return usuario;
            }
        }

        throw new RuntimeException("Correo o contraseña inválidos");
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> obtenerTecnicos() {
        return usuarioRepository.findByRol(Rol.TECNICO);
    }


}

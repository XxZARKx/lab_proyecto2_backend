package com.helpdesk.helpdesk.controller;

import com.helpdesk.helpdesk.model.Categoria;
import com.helpdesk.helpdesk.repository.CategoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USUARIO','TECNICO','ADMINISTRADOR')")
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crear(@RequestBody Categoria body) {
        if (body.getNombre() == null || body.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre es requerido");
        }
        if (categoriaRepository.existsByNombreIgnoreCase(body.getNombre())) {
            return ResponseEntity.status(409).body("La categoría ya existe");
        }
        Categoria saved = categoriaRepository.save(body);
        return ResponseEntity.ok(saved);
    }
}

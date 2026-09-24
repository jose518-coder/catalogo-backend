package com.wposs.catalogo.controlador;

import com.wposs.catalogo.modelo.Categoria;
import com.wposs.catalogo.servicio.CategoriaServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaControlador {

    private final CategoriaServicio servicio;

    public CategoriaControlador(CategoriaServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(servicio.buscarTodas());
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(
            @RequestBody Categoria categoria) {

        Categoria creada = servicio.guardar(categoria);

        URI location = URI.create(
                "/api/categorias/" + creada.getId());

        return ResponseEntity
                .created(location)
                .body(creada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        servicio.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
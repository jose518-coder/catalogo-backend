package com.wposs.catalogo.controlador;

import com.wposs.catalogo.dto.CategoriaDetalle;
import com.wposs.catalogo.dto.CategoriaNueva;
import com.wposs.catalogo.servicio.CategoriaServicio;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaControlador {

    private final CategoriaServicio categoriaServicio;

    public CategoriaControlador(
            CategoriaServicio categoriaServicio
    ) {
        this.categoriaServicio = categoriaServicio;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDetalle>> listar() {
        return ResponseEntity.ok(
                categoriaServicio.buscarTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDetalle> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                categoriaServicio.buscarPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<CategoriaDetalle> crear(
            @Valid @RequestBody CategoriaNueva dto
    ) {
        return ResponseEntity
                .status(201)
                .body(categoriaServicio.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDetalle> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaNueva dto
    ) {
        return ResponseEntity.ok(
                categoriaServicio.actualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        categoriaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
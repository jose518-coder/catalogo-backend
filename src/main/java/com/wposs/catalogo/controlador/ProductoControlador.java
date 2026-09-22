package com.wposs.catalogo.controlador;

import com.wposs.catalogo.modelo.Producto;
import com.wposs.catalogo.modelo.ProductoEstadisticas;
import com.wposs.catalogo.servicio.ProductoServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/productos")
public class ProductoControlador {

    private final ProductoServicio servicio;

    public ProductoControlador(ProductoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> buscarTodos(
            @RequestParam(required = false) String categoria) {

        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(
                    servicio.buscarPorCategoria(categoria)
            );
        }

        return ResponseEntity.ok(
                servicio.buscarTodos()
        );
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<ProductoEstadisticas> obtenerEstadisticas() {
        return ResponseEntity.ok(
                servicio.obtenerEstadisticas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(
            @PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    servicio.buscarPorId(id)
            );

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody Producto producto) {

        try {
            Producto creado = servicio.guardar(producto);

            URI ubicacion = URI.create(
                    "/api/productos/" + creado.id()
            );

            return ResponseEntity
                    .created(ubicacion)
                    .body(creado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Producto producto) {

        try {
            Producto actualizado =
                    servicio.actualizar(id, producto);

            return ResponseEntity.ok(actualizado);

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        try {
            servicio.eliminar(id);

            return ResponseEntity.noContent().build();

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
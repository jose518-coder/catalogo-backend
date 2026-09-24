package com.wposs.catalogo.controlador;

import com.wposs.catalogo.modelo.Producto;
import com.wposs.catalogo.modelo.ProductoEstadisticas;
import com.wposs.catalogo.servicio.ProductoServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoControlador {

    private final ProductoServicio servicio;

    public ProductoControlador(ProductoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar(
            @RequestParam(required = false) String categoria) {

        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(
                    servicio.buscarPorCategoria(categoria));
        }

        return ResponseEntity.ok(servicio.buscarTodos());
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<ProductoEstadisticas> estadisticas() {
        return ResponseEntity.ok(
                servicio.obtenerEstadisticas());
    }

    @GetMapping("/sin-stock")
    public ResponseEntity<List<Producto>> sinStock(
            @RequestParam(defaultValue = "5") Integer limite) {

        return ResponseEntity.ok(
                servicio.buscarSinStock(limite));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                servicio.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(
            @RequestBody Producto producto) {

        Producto creado = servicio.guardar(producto);

        URI location = URI.create(
                "/api/productos/" + creado.getId());

        return ResponseEntity
                .created(location)
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @RequestBody Producto producto) {

        return ResponseEntity.ok(
                servicio.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        servicio.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
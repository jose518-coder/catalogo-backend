package com.wposs.catalogo.controlador;

import com.wposs.catalogo.dto.*;
import com.wposs.catalogo.servicio.ProductoServicio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Validated
public class ProductoControlador {

    private final ProductoServicio productoServicio;

    public ProductoControlador(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResumen>> listar() {
        return ResponseEntity.ok(
                productoServicio.buscarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDetalle> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                productoServicio.buscarPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<ProductoDetalle> crear(
            @Valid @RequestBody ProductoNuevo dto
    ) {
        ProductoDetalle creado =
                productoServicio.guardar(dto);

        URI location = URI.create(
                "/api/productos/" + creado.id()
        );

        return ResponseEntity
                .created(location)
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDetalle> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoActualizar dto
    ) {
        return ResponseEntity.ok(
                productoServicio.actualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        productoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasCatalogo> estadisticas() {
        return ResponseEntity.ok(
                productoServicio.obtenerEstadisticas()
        );
    }

    @GetMapping("/categoria/{nombre}")
    public ResponseEntity<List<ProductoDetalle>> porCategoria(
            @PathVariable String nombre
    ) {
        return ResponseEntity.ok(
                productoServicio.buscarPorCategoria(nombre)
        );
    }

    @GetMapping("/precio")
    public ResponseEntity<List<ProductoDetalle>> porPrecio(
            @RequestParam BigDecimal minimo,
            @RequestParam BigDecimal maximo
    ) {
        return ResponseEntity.ok(
                productoServicio.buscarPorPrecio(minimo, maximo)
        );
    }

    @GetMapping("/sin-stock")
    public ResponseEntity<List<ProductoDetalle>> sinStock(
            @RequestParam @Min(
                    value = 1,
                    message = "El límite debe ser mayor o igual a 1"
            ) int limite
    ) {
        return ResponseEntity.ok(
                productoServicio.buscarSinStock(limite)
        );
    }
}
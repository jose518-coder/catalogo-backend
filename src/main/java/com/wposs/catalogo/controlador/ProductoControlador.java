package com.wposs.catalogo.controlador;

import com.wposs.catalogo.dto.*;
import com.wposs.catalogo.servicio.ProductoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Productos",
        description = "Operaciones para administrar y consultar productos del catálogo"
)
public class ProductoControlador {

    private final ProductoServicio productoServicio;

    public ProductoControlador(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    @GetMapping
    @Operation(
            summary = "Listar productos",
            description = "Obtiene la lista de todos los productos del catálogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    })
    public ResponseEntity<List<ProductoResumen>> listar() {
        return ResponseEntity.ok(
                productoServicio.buscarTodos()
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar producto por ID",
            description = "Obtiene el detalle de un producto mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ProductoDetalle> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                productoServicio.buscarPorId(id)
        );
    }

    @PostMapping
    @Operation(
            summary = "Crear producto",
            description = "Registra un nuevo producto en el catálogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    })
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
    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza los datos de un producto existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ProductoDetalle> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoActualizar dto
    ) {
        return ResponseEntity.ok(
                productoServicio.actualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto del catálogo mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar productos")
    })
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        productoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estadisticas")
    @Operation(
            summary = "Obtener estadísticas",
            description = "Obtiene las estadísticas generales del catálogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente")
    })
    public ResponseEntity<EstadisticasCatalogo> estadisticas() {
        return ResponseEntity.ok(
                productoServicio.obtenerEstadisticas()
        );
    }

    @GetMapping("/categoria/{nombre}")
    @Operation(
            summary = "Buscar productos por categoría",
            description = "Obtiene los productos que pertenecen a una categoría específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente")
    })
    public ResponseEntity<List<ProductoDetalle>> porCategoria(
            @PathVariable String nombre
    ) {
        return ResponseEntity.ok(
                productoServicio.buscarPorCategoria(nombre)
        );
    }

    @GetMapping("/precio")
    @Operation(
            summary = "Buscar productos por rango de precio",
            description = "Obtiene los productos cuyo precio está entre el mínimo y el máximo indicados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "Rango de precios inválido")
    })
    public ResponseEntity<List<ProductoDetalle>> porPrecio(
            @RequestParam BigDecimal minimo,
            @RequestParam BigDecimal maximo
    ) {
        return ResponseEntity.ok(
                productoServicio.buscarPorPrecio(minimo, maximo)
        );
    }

    @GetMapping("/sin-stock")
    @Operation(
            summary = "Consultar productos sin stock",
            description = "Obtiene productos sin existencias, limitado por la cantidad indicada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "El límite debe ser mayor o igual a 1")
    })
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
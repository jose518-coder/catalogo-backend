package com.wposs.catalogo.controlador;

import com.wposs.catalogo.dto.CategoriaDetalle;
import com.wposs.catalogo.dto.CategoriaNueva;
import com.wposs.catalogo.servicio.CategoriaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(
        name = "Categorías",
        description = "Operaciones para consultar y administrar las categorías"
)
public class CategoriaControlador {

    private final CategoriaServicio categoriaServicio;

    public CategoriaControlador(
            CategoriaServicio categoriaServicio
    ) {
        this.categoriaServicio = categoriaServicio;
    }

    @GetMapping
    @Operation(
            summary = "Listar categorías",
            description = "Obtiene todas las categorías registradas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categorías obtenidas correctamente"
            )
    })
    public ResponseEntity<List<CategoriaDetalle>> listar() {
        return ResponseEntity.ok(
                categoriaServicio.buscarTodas()
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar categoría por ID",
            description = "Obtiene una categoría mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada"
            )
    })
    public ResponseEntity<CategoriaDetalle> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                categoriaServicio.buscarPorId(id)
        );
    }

    @PostMapping
    @Operation(
            summary = "Crear categoría",
            description = "Registra una nueva categoría."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Categoría creada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de la categoría inválidos"
            )
    })
    public ResponseEntity<CategoriaDetalle> crear(
            @Valid @RequestBody CategoriaNueva dto
    ) {
        return ResponseEntity
                .status(201)
                .body(categoriaServicio.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar categoría",
            description = "Actualiza los datos de una categoría existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría actualizada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de la categoría inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada"
            )
    })
    public ResponseEntity<CategoriaDetalle> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaNueva dto
    ) {
        return ResponseEntity.ok(
                categoriaServicio.actualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar categoría",
            description = "Elimina una categoría mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Categoría eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No tiene permisos para eliminar categorías"
            )
    })
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        categoriaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información detallada de una categoría")
public record CategoriaDetalle(

        @Schema(description = "Identificador único de la categoría", example = "1")
        Long id,

        @Schema(description = "Nombre de la categoría", example = "Accesorios")
        String nombre,

        @Schema(description = "Descripción de la categoría",
                example = "Accesorios para computadores y dispositivos")
        String descripcion,

        @Schema(description = "Cantidad de productos asociados a la categoría",
                example = "15")
        int cantidadProductos

) {
}
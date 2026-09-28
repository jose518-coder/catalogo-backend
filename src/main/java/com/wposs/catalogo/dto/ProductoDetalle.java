package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Información detallada de un producto")
public record ProductoDetalle(

        @Schema(description = "Identificador único del producto", example = "1")
        Long id,

        @Schema(description = "Título del producto", example = "Teclado mecánico")
        String titulo,

        @Schema(description = "Precio del producto", example = "85000.00")
        BigDecimal precio,

        @Schema(description = "Cantidad de unidades disponibles", example = "25")
        Integer existencias,

        @Schema(description = "Nombre de la categoría del producto", example = "Accesorios")
        String categoria

) {
}
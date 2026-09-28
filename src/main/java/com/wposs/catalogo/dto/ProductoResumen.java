package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Información resumida de un producto")
public record ProductoResumen(

        @Schema(description = "Identificador único del producto", example = "1")
        Long id,

        @Schema(description = "Título del producto", example = "Teclado mecánico")
        String titulo,

        @Schema(description = "Precio del producto", example = "85000.00")
        BigDecimal precio

) {
}
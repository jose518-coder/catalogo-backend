package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "Estadísticas generales del catálogo de productos")
public record EstadisticasCatalogo(

        @Schema(description = "Cantidad total de productos", example = "50")
        int totalProductos,

        @Schema(description = "Valor total del inventario", example = "4250000.00")
        BigDecimal valorTotalInventario,

        @Schema(
                description = "Cantidad de productos agrupados por categoría",
                example = "{\"Accesorios\": 20, \"Computadores\": 30}"
        )
        Map<String, Integer> cantidadPorCategoria

) {
}
package com.wposs.catalogo.dto;

import java.math.BigDecimal;
import java.util.Map;

public record EstadisticasCatalogo(
        int totalProductos,
        BigDecimal valorTotalInventario,
        Map<String, Integer> cantidadPorCategoria
) {
}
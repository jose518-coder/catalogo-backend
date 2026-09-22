package com.wposs.catalogo.modelo;

import java.math.BigDecimal;
import java.util.Map;

public record ProductoEstadisticas(
        int totalProductos,
        BigDecimal valorTotalInventario,
        Map<String, Integer> cantidadPorCategoria
) {
}
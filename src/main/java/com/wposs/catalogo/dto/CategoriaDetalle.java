package com.wposs.catalogo.dto;

public record CategoriaDetalle(
        Long id,
        String nombre,
        String descripcion,
        int cantidadProductos
) {
}
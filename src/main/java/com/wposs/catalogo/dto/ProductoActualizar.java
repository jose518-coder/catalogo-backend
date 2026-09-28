package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Datos para actualizar un producto existente")
public record ProductoActualizar(

        @Schema(description = "Título del producto", example = "Teclado mecánico")
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 120, message = "El título no puede superar los 120 caracteres")
        String titulo,

        @Schema(description = "Precio del producto", example = "85000.00")
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
        @Digits(
                integer = 10,
                fraction = 2,
                message = "El precio debe tener máximo 10 enteros y 2 decimales"
        )
        BigDecimal precio,

        @Schema(description = "Cantidad de unidades disponibles", example = "25")
        @NotNull(message = "Las existencias son obligatorias")
        @PositiveOrZero(message = "Las existencias no pueden ser negativas")
        Integer existencias,

        @Schema(description = "Identificador de la categoría", example = "1")
        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId
) {
}
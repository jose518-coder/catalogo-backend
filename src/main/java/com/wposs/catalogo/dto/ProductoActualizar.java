package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Datos para actualizar un producto existente")
public record ProductoActualizar(

        @Schema(description = "Título del producto", example = "Teclado mecánico")
        @NotBlank(message = "El título es obligatorio")
        @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
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
        Long categoriaId,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(min = 20, max = 500, message = "La descripción debe tener entre 20 y 500 caracteres")
        String descripcion,

        @Size(max = 10, message = "No se pueden agregar más de 10 imágenes")
        List<@NotBlank(message = "La URL de la imagen es obligatoria") @Size(max = 2048) String> imagenes
) {
    public ProductoActualizar {
        imagenes = imagenes == null ? List.of() : List.copyOf(imagenes);
    }

    public ProductoActualizar(String titulo, BigDecimal precio,
                              Integer existencias, Long categoriaId) {
        this(titulo, precio, existencias, categoriaId,
                "Descripción pendiente para " + titulo + ".", List.of());
    }
}

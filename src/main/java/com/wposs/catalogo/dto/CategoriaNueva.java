package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para crear o actualizar una categoría")
public record CategoriaNueva(

        @Schema(description = "Nombre de la categoría", example = "Accesorios")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(
                min = 3,
                max = 60,
                message = "El nombre debe tener entre 3 y 60 caracteres"
        )
        String nombre,

        @Schema(description = "Descripción de la categoría", example = "Accesorios para computadores y dispositivos")
        @Size(
                max = 500,
                message = "La descripción no puede superar los 500 caracteres"
        )
        String descripcion
) {
}
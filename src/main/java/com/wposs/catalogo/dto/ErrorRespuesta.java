package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

@Schema(description = "Información detallada de un error de la API")
public record ErrorRespuesta(

        @Schema(description = "Fecha y hora en que ocurrió el error",
                example = "2026-09-27T15:30:00Z")
        Instant momento,

        @Schema(description = "Código HTTP del error", example = "400")
        int estado,

        @Schema(description = "Descripción del tipo de error",
                example = "Bad Request")
        String error,

        @Schema(description = "Mensaje explicativo del error",
                example = "Los datos enviados no son válidos")
        String mensaje,

        @Schema(description = "Ruta donde ocurrió el error",
                example = "/api/productos")
        String ruta,

        @Schema(
                description = "Errores de validación asociados a campos específicos",
                example = "{\"titulo\": \"El título es obligatorio\"}"
        )
        Map<String, String> campos

) {
}
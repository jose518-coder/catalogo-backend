package com.wposs.catalogo.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorRespuesta(
        Instant momento,
        int estado,
        String error,
        String mensaje,
        String ruta,
        Map<String, String> campos
) {
}
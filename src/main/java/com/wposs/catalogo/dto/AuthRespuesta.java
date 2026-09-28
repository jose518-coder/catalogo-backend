package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticación del usuario")
public record AuthRespuesta(
        @Schema(description = "Token JWT para acceder a los recursos protegidos",
                example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,
        @Schema(description = "Tipo de token utilizado",
                example = "Bearer")
        String tipo,
        @Schema(description = "Nombre del usuario autenticado",
                example = "jose123")
        String usuario,
        @Schema(description = "Rol asignado al usuario",
                example = "USER")
        String rol
) {
}
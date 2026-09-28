package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información del perfil del usuario autenticado")
public record PerfilRespuesta(
        @Schema(description = "Identificador único del usuario",
                example = "1")
        Long id,
        @Schema(description = "Nombre de usuario",
                example = "juan123")
        String usuario,
        @Schema(description = "Correo electrónico del usuario",
                example = "juan@gmail.com")
        String correo,
        @Schema(description = "Rol asignado al usuario",
                example = "USER")
        String rol,
        @Schema(description = "Indica si la cuenta del usuario está activa",
                example = "true")
        boolean activo
) {
}
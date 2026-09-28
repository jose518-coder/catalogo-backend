package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales para iniciar sesión")
public record LoginSolicitud(

        @Schema(description = "Nombre de usuario", example = "jose123")
        @NotBlank
        String usuario,

        @Schema(description = "Contraseña del usuario", example = "Clave123*")
        @NotBlank
        String contrasena
) {
}
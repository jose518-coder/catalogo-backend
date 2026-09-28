package com.wposs.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar un usuario")
public record RegistroSolicitud(

        @Schema(description = "Nombre de usuario", example = "jose123")
        @NotBlank
        @Size(max = 60)
        String usuario,

        @Schema(description = "Contraseña de acceso", example = "Clave123*")
        @NotBlank
        @Size(min = 8, max = 100)
        String contrasena,

        @Schema(description = "Correo electrónico del usuario", example = "jose@gmail.com")
        @NotBlank
        @Email
        @Size(max = 120)
        String correo
) {
}
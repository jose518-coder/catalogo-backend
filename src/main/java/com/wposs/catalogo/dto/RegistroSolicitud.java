package com.wposs.catalogo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroSolicitud(
        @NotBlank
        @Size(max = 60)
        String usuario,

        @NotBlank
        @Size(min = 8, max = 100)
        String contrasena,

        @NotBlank
        @Email
        @Size(max = 120)
        String correo
) {
}
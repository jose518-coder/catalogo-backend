package com.wposs.catalogo.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginSolicitud(
        @NotBlank
        String usuario,

        @NotBlank
        String contrasena
) {
}
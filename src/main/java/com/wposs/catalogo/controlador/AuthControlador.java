package com.wposs.catalogo.controlador;

import com.wposs.catalogo.dto.AuthRespuesta;
import com.wposs.catalogo.dto.LoginSolicitud;
import com.wposs.catalogo.dto.RegistroSolicitud;
import com.wposs.catalogo.dto.PerfilRespuesta;
import com.wposs.catalogo.servicio.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthControlador {

    private final AuthService authService;

    public AuthControlador(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<AuthRespuesta> registrar(
            @Valid @RequestBody RegistroSolicitud solicitud) {

        AuthRespuesta respuesta = authService.registrar(solicitud);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthRespuesta> iniciarSesion(
            @Valid @RequestBody LoginSolicitud solicitud) {

        AuthRespuesta respuesta =
                authService.iniciarSesion(solicitud);

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/yo")
    public ResponseEntity<PerfilRespuesta> obtenerMiPerfil(
            org.springframework.security.core.Authentication autenticacion) {

        PerfilRespuesta perfil =
                authService.obtenerPerfil(autenticacion.getName());

        return ResponseEntity.ok(perfil);
    }
}
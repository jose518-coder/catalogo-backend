package com.wposs.catalogo.servicio;

import com.wposs.catalogo.dto.AuthRespuesta;
import com.wposs.catalogo.dto.LoginSolicitud;
import com.wposs.catalogo.dto.RegistroSolicitud;
import com.wposs.catalogo.modelo.Rol;
import com.wposs.catalogo.dto.PerfilRespuesta;
import com.wposs.catalogo.modelo.Usuario;
import com.wposs.catalogo.repositorio.UsuarioRepositorio;
import com.wposs.catalogo.seguridad.ServicioJwt;
import com.wposs.catalogo.seguridad.UsuarioDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsuarioDetailsService usuarioDetailsService;
    private final ServicioJwt servicioJwt;

    public AuthService(
            UsuarioRepositorio usuarioRepositorio,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UsuarioDetailsService usuarioDetailsService,
            ServicioJwt servicioJwt) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.usuarioDetailsService = usuarioDetailsService;
        this.servicioJwt = servicioJwt;
    }

    public AuthRespuesta registrar(RegistroSolicitud solicitud) {
        if (usuarioRepositorio.existsByUsuario(solicitud.usuario())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario ya está registrado"
            );
        }

        Usuario usuario = new Usuario();
        usuario.setUsuario(solicitud.usuario());
        usuario.setContrasena(
                passwordEncoder.encode(solicitud.contrasena())
        );
        usuario.setCorreo(solicitud.correo());
        usuario.setRol(Rol.USER);
        usuario.setActivo(true);

        usuarioRepositorio.save(usuario);

        UserDetails detalles = usuarioDetailsService
                .loadUserByUsername(usuario.getUsuario());

        String token = servicioJwt.generar(detalles);

        return new AuthRespuesta(
                token,
                "Bearer",
                usuario.getUsuario(),
                usuario.getRol().name()
        );
    }

    public AuthRespuesta iniciarSesion(LoginSolicitud solicitud) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            solicitud.usuario(),
                            solicitud.contrasena()
                    )
            );
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "usuario o contraseña incorrectos"
            );
        }

        UserDetails detalles = usuarioDetailsService
                .loadUserByUsername(solicitud.usuario());

        Usuario usuario = usuarioRepositorio
                .findByUsuario(solicitud.usuario())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "usuario o contraseña incorrectos"
                ));

        String token = servicioJwt.generar(detalles);

        return new AuthRespuesta(
                token,
                "Bearer",
                usuario.getUsuario(),
                usuario.getRol().name()
        );
    }
    
    public PerfilRespuesta obtenerPerfil(String nombreUsuario) {
    Usuario usuario = usuarioRepositorio
            .findByUsuario(nombreUsuario)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuario no encontrado"
            ));

    return new PerfilRespuesta(
            usuario.getId(),
            usuario.getUsuario(),
            usuario.getCorreo(),
            usuario.getRol().name(),
            usuario.isActivo()
    );
}
}
package com.wposs.catalogo.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroJwt extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(FiltroJwt.class);

    private final ServicioJwt servicioJwt;
    private final UsuarioDetailsService usuarioDetailsService;

    public FiltroJwt(
            ServicioJwt servicioJwt,
            UsuarioDetailsService usuarioDetailsService) {
        this.servicioJwt = servicioJwt;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String encabezado = request.getHeader("Authorization");

        if (encabezado == null || !encabezado.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = encabezado.substring(7);

        try {
            String nombreUsuario = servicioJwt.extraerUsuario(token);

            if (SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                UserDetails usuario = usuarioDetailsService
                        .loadUserByUsername(nombreUsuario);

                if (servicioJwt.esValido(token, usuario)) {
                    UsernamePasswordAuthenticationToken autenticacion =
                            new UsernamePasswordAuthenticationToken(
                                    usuario,
                                    null,
                                    usuario.getAuthorities()
                            );

                    autenticacion.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(autenticacion);
                }
            }
        } catch (io.jsonwebtoken.JwtException |
                 IllegalArgumentException |
                 org.springframework.security.core.userdetails
                         .UsernameNotFoundException e) {
            log.debug("No se pudo validar el token JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
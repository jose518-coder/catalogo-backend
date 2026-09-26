package com.wposs.catalogo.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class ServicioJwt {

    private final SecretKey clave;
    private final long duracionMs;

    public ServicioJwt(
            @Value("${app.jwt.secreto}") String secreto,
            @Value("${app.jwt.duracion-ms}") long duracionMs) {

        if (secreto == null ||
                secreto.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException(
                    "El secreto JWT debe tener al menos 32 bytes"
            );
        }

        this.clave = Keys.hmacShaKeyFor(
                secreto.getBytes(StandardCharsets.UTF_8)
        );
        this.duracionMs = duracionMs;
    }

    public String generar(UserDetails usuario) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + duracionMs);

        List<String> roles = usuario.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("roles", roles)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(clave)
                .compact();
    }

    public String extraerUsuario(String token) {
        return extraerClaims(token).getSubject();
    }

    public boolean esValido(String token, UserDetails usuario) {
        final String nombreUsuario;

        try {
            nombreUsuario = extraerUsuario(token);
        } catch (io.jsonwebtoken.JwtException |
                 IllegalArgumentException e) {
            return false;
        }

        return nombreUsuario.equals(usuario.getUsername())
                && extraerClaims(token).getExpiration().after(new Date());
    }

    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
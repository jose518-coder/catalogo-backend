package com.wposs.catalogo.seguridad;

import com.wposs.catalogo.modelo.Usuario;
import com.wposs.catalogo.repositorio.UsuarioRepositorio;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioDetailsService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String usuario)
            throws UsernameNotFoundException {

        Usuario usuarioEncontrado = usuarioRepositorio
                .findByUsuario(usuario)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado"
                ));

        return User.builder()
                .username(usuarioEncontrado.getUsuario())
                .password(usuarioEncontrado.getContrasena())
                .roles(usuarioEncontrado.getRol().name())
                .disabled(!usuarioEncontrado.isActivo())
                .build();
    }
}
package com.wposs.catalogo.seguridad;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wposs.catalogo.dto.ErrorRespuesta;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final FiltroJwt filtroJwt;
    private final List<String> origenesPermitidos;
    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(
            FiltroJwt filtroJwt,
            UsuarioDetailsService usuarioDetailsService,
            @Value("${app.cors.origenes:http://localhost:4200}")
            String origenes) {
        this.filtroJwt = filtroJwt;
        this.usuarioDetailsService = usuarioDetailsService;
        this.origenesPermitidos = Arrays.stream(origenes.split(","))
                .map(String::trim)
                .filter(origen -> !origen.isEmpty())
                .toList();
    }

    @Bean
    @Order(1)
    @Profile("dev")
    public SecurityFilterChain swaggerSecurityFilterChain(
            HttpSecurity http) throws Exception {
        http
            .securityMatcher(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**"
            )
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource())
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, exception) -> {
                    response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                    );
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    ErrorRespuesta error = new ErrorRespuesta(
                        Instant.now(),
                        401,
                        "Unauthorized",
                        "Se requiere autenticación",
                        request.getRequestURI(),
                        null
                    );
                    new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        error
                    );
                })
                .accessDeniedHandler((request, response, exception) -> {
                    response.setStatus(
                        HttpServletResponse.SC_FORBIDDEN
                    );
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    ErrorRespuesta error = new ErrorRespuesta(
                        Instant.now(),
                        403,
                        "Forbidden",
                        "No tienes permisos para realizar esta operación",
                        request.getRequestURI(),
                        null
                    );
                    new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        error
                    );
                })
            )
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/registro",
                    "/api/auth/login"
                ).permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/auth/yo"
                ).authenticated()
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/productos/**"
                ).permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/categorias/**"
                ).permitAll()
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/productos/**"
                ).hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/productos/**"
                ).hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/productos/**"
                ).hasRole("ADMIN")
                .requestMatchers("/api/categorias/**")
                .hasRole("ADMIN")
                .requestMatchers("/api/usuarios/**")
                .hasRole("ADMIN")
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                filtroJwt,
                UsernamePasswordAuthenticationFilter.class
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(origenesPermitidos);
        configuracion.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );
        configuracion.setAllowedHeaders(
            List.of("Authorization", "Content-Type")
        );
        configuracion.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource fuente =
            new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/**", configuracion);
        return fuente;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider proveedor =
            new DaoAuthenticationProvider();
        proveedor.setUserDetailsService(usuarioDetailsService);
        proveedor.setPasswordEncoder(passwordEncoder());
        return proveedor;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuracion)
            throws Exception {
        return configuracion.getAuthenticationManager();
    }
}
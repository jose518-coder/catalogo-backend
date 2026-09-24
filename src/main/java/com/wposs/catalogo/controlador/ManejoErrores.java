package com.wposs.catalogo.controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejoErrores {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> manejarArgumentoInvalido(
            IllegalArgumentException exception) {

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<Void> manejarNoEncontrado(
            java.util.NoSuchElementException exception) {

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Void> manejarConflicto(
            IllegalStateException exception) {

        return ResponseEntity.status(409).build();
    }
}
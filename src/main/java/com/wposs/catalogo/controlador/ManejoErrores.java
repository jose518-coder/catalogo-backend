package com.wposs.catalogo.controlador;

import com.wposs.catalogo.dto.ErrorRespuesta;
import com.wposs.catalogo.excepcion.RecursoDuplicadoException;
import com.wposs.catalogo.excepcion.RecursoNoEncontradoException;
import com.wposs.catalogo.excepcion.ReglaDeNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejoErrores {
    private static final Logger log =
            LoggerFactory.getLogger(ManejoErrores.class);
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> manejarNoEncontrado(
            RecursoNoEncontradoException ex,
            HttpServletRequest request
    ) {
        return respuesta(
                ex.getEstado(),
                HttpStatus.valueOf(ex.getEstado()).getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorRespuesta> manejarDuplicado(
            RecursoDuplicadoException ex,
            HttpServletRequest request
    ) {
        return respuesta(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<ErrorRespuesta> manejarReglaNegocio(
            ReglaDeNegocioException ex,
            HttpServletRequest request
    ) {
        return respuesta(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> manejarValidacion(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> campos = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        campos.put(error.getField(), error.getDefaultMessage())
                );

        return respuesta(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "La solicitud contiene datos inválidos",
                request.getRequestURI(),
                campos
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorRespuesta> manejarRestriccion(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        Map<String, String> campos = new LinkedHashMap<>();

        ex.getConstraintViolations()
                .forEach(error ->
                        campos.put(
                                error.getPropertyPath().toString(),
                                error.getMessage()
                        )
                );

        return respuesta(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "La solicitud contiene datos inválidos",
                request.getRequestURI(),
                campos
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> manejarJsonInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        return respuesta(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "El cuerpo de la solicitud no es válido",
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> manejarTipoIncorrecto(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        return respuesta(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "El parámetro tiene un formato inválido",
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuesta> manejarIntegridad(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        log.warn("Violación de integridad de datos", ex);

        return respuesta(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "No se puede completar la operación por una restricción de datos",
                request.getRequestURI(),
                null
        );
    }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ErrorRespuesta> manejarRecursoNoEncontrado(
                NoResourceFoundException ex,
                HttpServletRequest request
        ) {
        return respuesta(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "El recurso solicitado no existe",
                request.getRequestURI(),
                null
        );
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarErrorGeneral(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Error inesperado en la aplicación", ex);

        return respuesta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocurrió un error interno del servidor",
                request.getRequestURI(),
                null
        );
    }

    private ResponseEntity<ErrorRespuesta> respuesta(
            int estado,
            String error,
            String mensaje,
            String ruta,
            Map<String, String> campos
    ) {
        ErrorRespuesta body = new ErrorRespuesta(
                Instant.now(),
                estado,
                error,
                mensaje,
                ruta,
                campos
        );

        return ResponseEntity
                .status(estado)
                .body(body);
    }
}
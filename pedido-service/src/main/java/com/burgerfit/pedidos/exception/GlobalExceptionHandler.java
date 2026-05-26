package com.burgerfit.pedidos.exception;

import com.burgerfit.pedidos.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> manejarResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> manejarBadRequest(
            BadRequestException ex,
            HttpServletRequest request
    ) {
        log.warn("Solicitud inválida: {}", ex.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud inválida",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> manejarErroresValidacion(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        log.warn("Error de validación en la solicitud");

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                "Uno o más campos tienen errores",
                request.getRequestURI(),
                errores
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ErrorResponseDTO> manejarErrorConexionMicroservicio(
            WebClientRequestException ex,
            HttpServletRequest request
    ) {
        log.error("Error de conexión con otro microservicio: {}", ex.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Microservicio no disponible",
                "No se pudo establecer conexión con un microservicio externo",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponseDTO> manejarErrorRespuestaMicroservicio(
            WebClientResponseException ex,
            HttpServletRequest request
    ) {
        log.error("Error de respuesta desde otro microservicio. Status: {}", ex.getStatusCode());

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                ex.getStatusCode().value(),
                "Error al consultar microservicio externo",
                "El microservicio externo respondió con error",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> manejarErroresGenerales(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Error interno no controlado: {}", ex.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                "Ocurrió un error inesperado",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
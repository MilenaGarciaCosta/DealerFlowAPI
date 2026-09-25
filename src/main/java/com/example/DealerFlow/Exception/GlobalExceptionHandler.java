package com.example.DealerFlow.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("AUTH_FAILURE: Falha de autenticação/credenciais. Motivo: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a, b) -> a));
        log.warn("VALIDATION_FAILURE: Requisição rejeitada por dados inválidos: {}", errors);
        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }

    @ExceptionHandler(ConsultInputException.class)
    public ResponseEntity<Map<String, String>> handleConsultInput(ConsultInputException ex) {
        log.warn("VALIDATION_FAILURE: Requisição rejeitada por parâmetro inválido. Motivo: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(PythonApiException.class)
    public ResponseEntity<Map<String, String>> handlePythonApi(PythonApiException ex) {
        if (ex.getStatus().is5xxServerError()) {
            log.error("PYTHON_API_FAILURE: Falha ao consultar a API Python. Status: {}. Motivo: {}",
                    ex.getStatus().value(), ex.getMessage(), ex);
        } else {
            log.warn("PYTHON_API_FAILURE: Recurso não encontrado na API Python. Status: {}. Motivo: {}",
                    ex.getStatus().value(), ex.getMessage());
        }

        return ResponseEntity.status(ex.getStatus()).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("VALIDATION_FAILURE: Tipo de parâmetro inválido: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid request parameter"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParameter(
            MissingServletRequestParameterException ex) {
        log.warn("VALIDATION_FAILURE: Parâmetro obrigatório ausente: {}", ex.getParameterName());
        return ResponseEntity.badRequest().body(Map.of("message", "Required request parameter is missing"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllUncaughtException(Exception ex) {
        log.error("SYSTEM_FAILURE: Ocorreu um erro interno inesperado na API. ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Ocorreu um erro interno no servidor. A equipe técnica foi notificada."));
    }
}

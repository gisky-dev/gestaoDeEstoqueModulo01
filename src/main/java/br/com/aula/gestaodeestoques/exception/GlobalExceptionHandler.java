package br.com.aula.gestaodeestoques.exception;

import br.com.aula.gestaodeestoques.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handler para erros de validação (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        // Corpo da resposta para erros de validação, incluindo detalhes de cada campo
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", Instant.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", "Erro de Validação");
        responseBody.put("message", "Um ou mais campos contêm dados inválidos.");
        responseBody.put("fieldErrors", fieldErrors);
        responseBody.put("path", request.getRequestURI());

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    // Handler para nossa exceção customizada de "não encontrado"
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso Não Encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Handler genérico para qualquer outra exceção não tratada (Erro 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno no Servidor",
                "Ocorreu um erro inesperado. Por favor, contate o suporte.",
                request.getRequestURI()
        );
        // Em um sistema real, é crucial logar a exceção completa aqui
        // ex.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

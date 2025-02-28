package dev.yuri.votacao.advice;

import com.fasterxml.jackson.databind.JsonMappingException;
import dev.yuri.votacao.client.exception.InvalidCpfException;
import dev.yuri.votacao.dto.response.ErroResponse;
import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.exception.SessionClosedException;
import dev.yuri.votacao.exception.SessionNotFinishedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ErroResponse> buildErrorResponse(HttpStatus status, String message, String details, WebRequest request, List<ErroResponse.FieldError> fieldErrors) {
        return ResponseEntity.status(status).body(new ErroResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getDescription(false),
                fieldErrors
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErroResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new ErroResponse.FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        log.warn("Erro de validação: {}", fieldErrors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de validação", "Verifique os campos informados.", request, fieldErrors);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ErroResponse> handleJsonMappingException(JsonMappingException ex, WebRequest request) {
        log.warn("Erro de mapeamento de JSON: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Data inválida. Use o formato dd/MM/yyyy HH:mm",
                "O formato de data fornecido não corresponde ao esperado.", request, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        if (ex.getCause() instanceof JsonMappingException) {
            return handleJsonMappingException((JsonMappingException) ex.getCause(), request);
        }

        log.warn("Requisição com corpo inválido: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido", "Verifique o formato do JSON.", request, null);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "O recurso solicitado não foi encontrado.", request, null);
    }

    @ExceptionHandler(InvalidCpfException.class)
    public ResponseEntity<ErroResponse> handleInvalidCpfException(InvalidCpfException ex, WebRequest request) {
        log.warn("CPF inválido: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "O CPF fornecido é inválido.", request, null);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErroResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex, WebRequest request) {
        log.warn("Entidade já existente: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), "O recurso já existe no sistema.", request, null);
    }

    @ExceptionHandler({SessionClosedException.class, SessionNotFinishedException.class})
    public ResponseEntity<ErroResponse> handleSessionExceptions(RuntimeException ex, WebRequest request) {
        log.warn("Erro relacionado à sessão: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), "Erro relacionado ao status da sessão.", request, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.warn("Valor inválido fornecido: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Valor inválido fornecido", ex.getMessage(), request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        log.warn("Erro de integridade de dados: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Erro de integridade de dados", "Verifique se os dados fornecidos violam alguma restrição do banco.", request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Erro interno do servidor: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor", "Ocorreu um erro inesperado. Tente novamente mais tarde.", request, null);
    }
}

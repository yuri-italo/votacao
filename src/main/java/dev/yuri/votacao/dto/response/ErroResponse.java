package dev.yuri.votacao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ErroResponse {

    @Schema(description = "Data e hora do erro", example = "28/02/2025 14:30")
    private LocalDateTime timestamp;

    @Schema(description = "Código de status HTTP", example = "400")
    private int status;

    @Schema(description = "Tipo do erro", example = "Bad Request")
    private String error;

    @Schema(description = "Mensagem de erro detalhada", example = "Formato de data inválido")
    private String message;

    @Schema(description = "Endpoint onde ocorreu o erro", example = "/api/v1/sessoes")
    private String path;

    @Schema(description = "Lista de erros nos campos da requisição")
    private List<FieldError> errors;

    @AllArgsConstructor
    @Getter
    public static class FieldError {

        @Schema(description = "Nome do campo com erro", example = "dataInicio")
        private String field;

        @Schema(description = "Mensagem de erro específica do campo", example = "A data deve estar no presente ou futuro")
        private String message;
    }
}

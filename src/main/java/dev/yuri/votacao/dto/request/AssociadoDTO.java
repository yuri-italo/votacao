package dev.yuri.votacao.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AssociadoDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        @Schema(description = "Nome do associado", example = "João Silva")
        String nome,

        @NotBlank(message = "O CPF não pode estar em branco")
        @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 caracteres")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter apenas números")
        @Schema(description = "CPF do associado (somente números)", example = "12345678901")
        String cpf
) {}

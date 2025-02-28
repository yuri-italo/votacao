package dev.yuri.votacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VotoDTO(
        @NotBlank(message = "A escolha não pode estar vazia")
        @Size(min = 3, max = 3, message = "O nome deve ter 3 caracteres")
        @Pattern(regexp = "SIM|NAO", message = "A escolha deve ser 'SIM' ou 'NAO'")
        @Schema(description = "Escolha do voto, deve ser 'SIM' ou 'NAO'", example = "SIM")
        String escolha,

        @NotNull(message = "A pauta associada é obrigatória")
        @Schema(description = "ID da pauta associada", example = "1")
        Long pautaId,

        @NotNull(message = "O associado é obrigatório")
        @Schema(description = "ID do associado que está votando", example = "10")
        Long associadoId
) {}

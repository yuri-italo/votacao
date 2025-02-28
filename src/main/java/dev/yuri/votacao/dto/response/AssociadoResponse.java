package dev.yuri.votacao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AssociadoResponse(
        @Schema(description = "ID do associado", example = "1")
        Long id,
        @Schema(description = "Nome do associado", example = "João Silva")
        String nome
) {}

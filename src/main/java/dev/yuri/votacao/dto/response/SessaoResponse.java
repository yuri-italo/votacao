package dev.yuri.votacao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record SessaoResponse(
        @Schema(description = "ID da sessão", example = "1")
        Long id,

        @Schema(description = "Data e hora de início da sessão", example = "28/02/2025 14:30")
        String dataInicio,

        @Schema(description = "Data e hora de fim da sessão", example = "28/02/2025 15:30")
        String dataFim,

        @Schema(description = "Status da sessão", example = "Aberta")
        String status,

        @Schema(description = "Pauta associada à sessão")
        PautaResponse pauta
) {}

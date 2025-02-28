package dev.yuri.votacao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record VotoResponse(
        @Schema(description = "ID do voto", example = "1")
        Long id,

        @Schema(description = "Nome da pauta associada ao voto", example = "Pauta de Votação A")
        String pauta,

        @Schema(description = "Nome do associado que votou", example = "Maria Oliveira")
        String nomeAssociado
) {}

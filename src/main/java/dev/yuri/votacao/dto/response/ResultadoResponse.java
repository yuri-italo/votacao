package dev.yuri.votacao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResultadoResponse(
        @Schema(description = "Nome da pauta", example = "Reforma do estatuto")
        String pauta,

        @Schema(description = "Quantidade de votos favoráveis", example = "150")
        Long votosFavoraveis,

        @Schema(description = "Quantidade de votos contra", example = "30")
        Long votosContra,

        @Schema(description = "Situação da votação", example = "Aprovada")
        String situacao
) {}

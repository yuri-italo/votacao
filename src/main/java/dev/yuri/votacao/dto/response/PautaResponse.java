package dev.yuri.votacao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PautaResponse(
        @Schema(description = "ID da pauta", example = "1")
        Long id,

        @Schema(description = "Nome da pauta", example = "Reforma do estatuto")
        String nome,

        @Schema(description = "Descrição detalhada da pauta", example = "Discussão sobre a atualização do estatuto da cooperativa.")
        String descricao
) {}

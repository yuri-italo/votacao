package dev.yuri.votacao.dto.request;

import dev.yuri.votacao.validation.DataFimMaiorQueDataInicio;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@DataFimMaiorQueDataInicio(
        dataInicioField = "dataInicio",
        dataFimField = "dataFim"
)
public record SessaoDTO(
        @FutureOrPresent(message = "A data de início deve ser no presente ou futuro")
        LocalDateTime dataInicio,

        @FutureOrPresent(message = "A data de fim deve ser no presente ou futuro")
        LocalDateTime dataFim,

        @NotNull(message = "A pauta associada é obrigatória")
        Long pautaId
) {}

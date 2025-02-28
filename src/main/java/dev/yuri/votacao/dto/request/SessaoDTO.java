package dev.yuri.votacao.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.yuri.votacao.validation.DataFimMaiorQueDataInicio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@DataFimMaiorQueDataInicio(
        dataInicioField = "dataInicio",
        dataFimField = "dataFim"
)
public record SessaoDTO(
        @FutureOrPresent(message = "A data de início deve ser no presente ou futuro")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @Schema(example = "28/02/2025 14:30")
        LocalDateTime dataInicio,

        @FutureOrPresent(message = "A data de fim deve ser no presente ou futuro")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @Schema(example = "28/02/2025 15:30")
        LocalDateTime dataFim,

        @NotNull(message = "A pauta associada é obrigatória")
        Long pautaId
) {}

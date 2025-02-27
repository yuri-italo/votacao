package dev.yuri.votacao.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VotoDTO(
        @NotBlank(message = "A escolha não pode estar vazia")
        @Size(min = 3, max = 3, message = "O nome deve ter 3 caracteres")
        @Pattern(regexp = "SIM|NAO", message = "A escolha deve ser 'SIM' ou 'NAO'")
        String escolha,

        @NotNull(message = "A pauta associada é obrigatória")
        Long pautaId,

        @NotNull(message = "O associado é obrigatório")
        Long associadoId
) {}

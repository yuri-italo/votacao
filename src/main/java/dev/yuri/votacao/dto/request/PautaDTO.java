package dev.yuri.votacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PautaDTO(
        @NotBlank(message = "O nome não pode estar vazio")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        @Schema(description = "Nome da pauta", example = "Revisão das taxas de associação")
        String nome,

        @NotBlank(message = "A descrição não pode estar vazia")
        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        @Schema(description = "Descrição detalhada da pauta",
                example = "Discussão sobre o ajuste das taxas de associação para o próximo ano.")
        String descricao
) {}

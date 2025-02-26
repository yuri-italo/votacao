package dev.yuri.votacao.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PautaDTO(
        @NotBlank(message = "O nome não pode estar vazio")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "A descrição não pode estar vazia")
        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String descricao
) {}

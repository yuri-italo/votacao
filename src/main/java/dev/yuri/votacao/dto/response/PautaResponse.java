package dev.yuri.votacao.dto.response;

public record PautaResponse(
        Long id,
        String nome,
        String descricao
) {}
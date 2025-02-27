package dev.yuri.votacao.dto.response;

public record ResultadoResponse(
        String pauta,
        Long votosFavoraveis,
        Long votosContra,
        String situacao
) {}

package dev.yuri.votacao.dto.response;

public record SessaoResponse(
        Long id,
        String dataInicio,
        String dataFim,
        String status,
        String nomePauta
) {}
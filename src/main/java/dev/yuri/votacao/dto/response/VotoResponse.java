package dev.yuri.votacao.dto.response;

public record VotoResponse(
        Long id,
        String pauta,
        String nomeAssociado
) {}
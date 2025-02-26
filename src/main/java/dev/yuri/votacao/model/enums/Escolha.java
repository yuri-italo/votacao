package dev.yuri.votacao.model.enums;

public enum Escolha {
    SIM("Sim"),
    NAO("Não");

    private final String descricao;

    Escolha(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
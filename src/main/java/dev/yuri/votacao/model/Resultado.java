package dev.yuri.votacao.model;

import dev.yuri.votacao.model.enums.Situacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    private Long quantidadeSim;

    private Long quantidadeNao;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    public Resultado() {}

    public void calcularSituacao() {
        long qtdSim = Objects.requireNonNullElse(quantidadeSim, 0L);
        long qtdNao = Objects.requireNonNullElse(quantidadeNao, 0L);

        if (qtdSim > qtdNao) {
            situacao = Situacao.APROVADA;
        } else if (qtdSim < qtdNao) {
            situacao = Situacao.REJEITADA;
        } else {
            situacao = Situacao.EMPATADA;
        }
    }

}

package dev.yuri.votacao.model;

import dev.yuri.votacao.model.enums.Situacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Entidade que representa o resultado de uma votação de uma pauta.
 * Contém informações sobre a quantidade de votos "Sim", "Não" e a situação da votação.
 * A situação da votação é calculada com base nos votos registrados.
 */
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

    /**
     * Situação da votação, que pode ser:
     * - APROVADA: Quando os votos "Sim" são maiores que os votos "Não".
     * - REJEITADA: Quando os votos "Não" são maiores que os votos "Sim".
     * - EMPATADA: Quando o número de votos "Sim" e "Não" é igual.
     */
    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    public Resultado() {}

    /**
     * Método que calcula a situação da votação com base na quantidade de votos "Sim" e "Não".
     * Define o valor da variável {@link #situacao} para uma das três opções:
     * - APROVADA: Caso haja mais votos "Sim".
     * - REJEITADA: Caso haja mais votos "Não".
     * - EMPATADA: Caso o número de votos "Sim" e "Não" seja igual.
     */
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

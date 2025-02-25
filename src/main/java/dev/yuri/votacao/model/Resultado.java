package dev.yuri.votacao.model;

import dev.yuri.votacao.model.enums.Situacao;
import jakarta.persistence.*;

@Entity
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
}

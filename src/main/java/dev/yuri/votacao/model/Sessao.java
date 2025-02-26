package dev.yuri.votacao.model;

import dev.yuri.votacao.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
public class Sessao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @PrePersist
    private void inicializarCampos() {
        if (this.dataInicio == null) {
            this.dataInicio = (this.dataFim != null) ? this.dataFim.minusMinutes(1) : LocalDateTime.now();
        }
        if (this.dataFim == null) {
            this.dataFim = this.dataInicio.plusMinutes(1);
        }
        atualizarStatus();
    }

    public void atualizarStatus() {
        LocalDateTime agora = LocalDateTime.now();
        this.status = (agora.isEqual(dataInicio) || (agora.isAfter(dataInicio) && agora.isBefore(dataFim)))
                ? Status.ABERTA
                : Status.FECHADA;
    }
}

package dev.yuri.votacao.model;

import dev.yuri.votacao.model.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "A data início não pode ser nula")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data fim não pode ser nula")
    private LocalDateTime dataFim;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status não pode ser nulo")
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

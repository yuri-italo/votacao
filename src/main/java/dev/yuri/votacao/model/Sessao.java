package dev.yuri.votacao.model;

import dev.yuri.votacao.model.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Sessao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data início não pode ser nula")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data fim não pode ser nula")
    private LocalDateTime dataFim;

    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    public Sessao() {}

    public Sessao(LocalDateTime dataInicio, LocalDateTime dataFim, Pauta pauta) {
        this.dataInicio = (dataInicio != null) ? dataInicio : LocalDateTime.now();
        this.dataFim = (dataFim != null) ? dataFim : this.dataInicio.plusMinutes(1);

        if (!this.dataFim.isAfter(this.dataInicio)) {
            throw new IllegalArgumentException("A data de fim deve ser maior que a data de início");
        }

        this.pauta = pauta;
    }

    public Status getStatus() {
        LocalDateTime agora = LocalDateTime.now();
        return (agora.isEqual(dataInicio) || (agora.isAfter(dataInicio) && agora.isBefore(dataFim)))
                ? Status.ABERTA
                : Status.FECHADA;
    }

    public boolean isOpen() {
        return this.getStatus().equals(Status.ABERTA);
    }
}

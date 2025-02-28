package dev.yuri.votacao.model;

import dev.yuri.votacao.model.enums.Escolha;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O voto não pode ser nulo")
    private Escolha escolha;

    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    @NotNull(message = "A pauta não pode ser nula")
    private Pauta pauta;

    @ManyToOne
    @JoinColumn(name = "associado_id", nullable = false)
    @NotNull(message = "O associado não pode ser nulo")
    private Associado associado;

    public Voto() {}
}
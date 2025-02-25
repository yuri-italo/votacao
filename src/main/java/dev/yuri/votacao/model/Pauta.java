package dev.yuri.votacao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode estar vazio")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "A descrição não pode estar vazia")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    private String descricao;
}

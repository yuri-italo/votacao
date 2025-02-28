package dev.yuri.votacao.model;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PautaTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testPautaComDadosValidos() {
        Pauta pauta = new Pauta();
        pauta.setNome("Reforma do Estacionamento");
        pauta.setDescricao("Discutir a reforma do estacionamento para melhorar a segurança.");

        Set<ConstraintViolation<Pauta>> violations = validator.validate(pauta);
        assertTrue(violations.isEmpty(), "Não deve haver violações para uma pauta com dados válidos");
    }

    @Test
    void testPautaComNomeVazio() {
        Pauta pauta = new Pauta();
        pauta.setNome("");
        pauta.setDescricao("Descrição válida.");

        Set<ConstraintViolation<Pauta>> violations = validator.validate(pauta);
        assertFalse(violations.isEmpty(), "Deve haver violações para um nome vazio");

        boolean mensagemEncontrada = violations.stream()
                .anyMatch(violation -> violation.getMessage().equals("O nome não pode estar vazio"));
        assertTrue(mensagemEncontrada, "A mensagem 'O nome não pode estar vazio' deve estar presente");
    }

    @Test
    void testPautaComNomeCurto() {
        Pauta pauta = new Pauta();
        pauta.setNome("A");
        pauta.setDescricao("Descrição válida.");

        Set<ConstraintViolation<Pauta>> violations = validator.validate(pauta);
        assertFalse(violations.isEmpty(), "Deve haver violações para um nome curto");
        assertEquals("O nome deve ter entre 3 e 100 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void testPautaComNomeLongo() {
        Pauta pauta = new Pauta();
        pauta.setNome("A".repeat(101));
        pauta.setDescricao("Descrição válida.");

        Set<ConstraintViolation<Pauta>> violations = validator.validate(pauta);
        assertFalse(violations.isEmpty(), "Deve haver violações para um nome longo");
        assertEquals("O nome deve ter entre 3 e 100 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void testPautaComDescricaoVazia() {
        Pauta pauta = new Pauta();
        pauta.setNome("Nome válido");
        pauta.setDescricao("");

        Set<ConstraintViolation<Pauta>> violations = validator.validate(pauta);
        assertFalse(violations.isEmpty(), "Deve haver violações para uma descrição vazia");
        assertEquals("A descrição não pode estar vazia", violations.iterator().next().getMessage());
    }

    @Test
    void testPautaComDescricaoLonga() {
        Pauta pauta = new Pauta();
        pauta.setNome("Nome válido");
        pauta.setDescricao("A".repeat(256));

        Set<ConstraintViolation<Pauta>> violations = validator.validate(pauta);
        assertFalse(violations.isEmpty(), "Deve haver violações para uma descrição longa");
        assertEquals("A descrição deve ter no máximo 255 caracteres", violations.iterator().next().getMessage());
    }
}
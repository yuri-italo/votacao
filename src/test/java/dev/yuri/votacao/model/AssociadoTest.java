package dev.yuri.votacao.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AssociadoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testAssociadoComNomeValido() {
        Associado associado = new Associado();
        associado.setNome("João Silva");
        associado.setCpf("12345678901");

        Set<ConstraintViolation<Associado>> violations = validator.validate(associado);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um nome válido");
    }

    @Test
    void testAssociadoComNomeVazio() {
        Associado associado = new Associado();
        associado.setNome("");
        associado.setCpf("12345678901");

        Set<ConstraintViolation<Associado>> violations = validator.validate(associado);
        assertFalse(violations.isEmpty(), "Deve haver violações para um nome vazio");
    }

    @Test
    void testAssociadoComNomeCurto() {
        Associado associado = new Associado();
        associado.setNome("Jo");
        associado.setCpf("12345678901");

        Set<ConstraintViolation<Associado>> violations = validator.validate(associado);
        assertFalse(violations.isEmpty(), "Deve haver violações para um nome curto");
        assertEquals("O nome deve ter entre 3 e 100 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void testAssociadoComNomeLongo() {
        Associado associado = new Associado();
        associado.setNome("João Silva".repeat(20));
        associado.setCpf("12345678901");

        Set<ConstraintViolation<Associado>> violations = validator.validate(associado);
        assertFalse(violations.isEmpty(), "Deve haver violações para um nome longo");
        assertEquals("O nome deve ter entre 3 e 100 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void testAssociadoComCPFVazio() {
        Associado associado = new Associado();
        associado.setNome("João Silva");
        associado.setCpf("");

        Set<ConstraintViolation<Associado>> violations = validator.validate(associado);
        assertFalse(violations.isEmpty(), "Deve haver violações para um CPF vazio");
        assertEquals("O CPF não pode estar vazio", violations.iterator().next().getMessage());
    }

    @Test
    void testAssociadoComCPFValido() {
        Associado associado = new Associado();
        associado.setNome("João Silva");
        associado.setCpf("12345678901");

        Set<ConstraintViolation<Associado>> violations = validator.validate(associado);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um CPF válido");
    }
}
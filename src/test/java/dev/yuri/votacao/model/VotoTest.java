package dev.yuri.votacao.model;

import dev.yuri.votacao.model.enums.Escolha;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VotoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testVotoComDadosValidos() {
        Voto voto = new Voto();
        voto.setEscolha(Escolha.SIM);
        voto.setPauta(new Pauta());
        voto.setAssociado(new Associado());

        Set<ConstraintViolation<Voto>> violations = validator.validate(voto);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um voto com dados válidos");
    }

    @Test
    void testVotoComEscolhaNula() {
        Voto voto = new Voto();
        voto.setEscolha(null); // Escolha nula
        voto.setPauta(new Pauta());
        voto.setAssociado(new Associado());

        Set<ConstraintViolation<Voto>> violations = validator.validate(voto);
        assertFalse(violations.isEmpty(), "Deve haver violações para uma escolha nula");
        assertEquals("O voto não pode ser nulo", violations.iterator().next().getMessage());
    }

    @Test
    void testVotoComPautaNula() {
        Voto voto = new Voto();
        voto.setEscolha(Escolha.SIM);
        voto.setPauta(null); // Pauta nula
        voto.setAssociado(new Associado());

        Set<ConstraintViolation<Voto>> violations = validator.validate(voto);
        assertFalse(violations.isEmpty(), "Deve haver violações para uma pauta nula");
        assertEquals("A pauta não pode ser nula", violations.iterator().next().getMessage());
    }

    @Test
    void testVotoComAssociadoNulo() {
        Voto voto = new Voto();
        voto.setEscolha(Escolha.SIM);
        voto.setPauta(new Pauta());
        voto.setAssociado(null); // Associado nulo

        Set<ConstraintViolation<Voto>> violations = validator.validate(voto);
        assertFalse(violations.isEmpty(), "Deve haver violações para um associado nulo");
        assertEquals("O associado não pode ser nulo", violations.iterator().next().getMessage());
    }
}
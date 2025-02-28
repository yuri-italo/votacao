package dev.yuri.votacao.model;

import dev.yuri.votacao.model.enums.Situacao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoTest {

    @Test
    void testCalcularSituacaoAprovada() {
        Resultado resultado = new Resultado();
        resultado.setQuantidadeSim(10L);
        resultado.setQuantidadeNao(5L);

        resultado.calcularSituacao();

        assertEquals(Situacao.APROVADA, resultado.getSituacao(), "A situação deve ser APROVADA quando há mais votos 'Sim'");
    }

    @Test
    void testCalcularSituacaoRejeitada() {
        Resultado resultado = new Resultado();
        resultado.setQuantidadeSim(5L);
        resultado.setQuantidadeNao(10L);

        resultado.calcularSituacao();

        assertEquals(Situacao.REJEITADA, resultado.getSituacao(), "A situação deve ser REJEITADA quando há mais votos 'Não'");
    }

    @Test
    void testCalcularSituacaoEmpatada() {
        Resultado resultado = new Resultado();
        resultado.setQuantidadeSim(10L);
        resultado.setQuantidadeNao(10L);

        resultado.calcularSituacao();

        assertEquals(Situacao.EMPATADA, resultado.getSituacao(), "A situação deve ser EMPATADA quando há o mesmo número de votos 'Sim' e 'Não'");
    }

    @Test
    void testCalcularSituacaoComVotosNulos() {
        Resultado resultado = new Resultado();
        resultado.setQuantidadeSim(null);
        resultado.setQuantidadeNao(null);

        resultado.calcularSituacao();

        assertEquals(Situacao.EMPATADA, resultado.getSituacao(), "A situação deve ser EMPATADA quando ambos os votos são nulos");
    }

    @Test
    void testCalcularSituacaoComVotosSimNulos() {
        Resultado resultado = new Resultado();
        resultado.setQuantidadeSim(null);
        resultado.setQuantidadeNao(10L);

        resultado.calcularSituacao();

        assertEquals(Situacao.REJEITADA, resultado.getSituacao(), "A situação deve ser REJEITADA quando os votos 'Sim' são nulos e há votos 'Não'");
    }

    @Test
    void testCalcularSituacaoComVotosNaoNulos() {
        Resultado resultado = new Resultado();
        resultado.setQuantidadeSim(10L);
        resultado.setQuantidadeNao(null);

        resultado.calcularSituacao();

        assertEquals(Situacao.APROVADA, resultado.getSituacao(), "A situação deve ser APROVADA quando os votos 'Não' são nulos e há votos 'Sim'");
    }
}
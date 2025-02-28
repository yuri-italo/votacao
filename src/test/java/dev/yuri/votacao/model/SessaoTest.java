package dev.yuri.votacao.model;

import dev.yuri.votacao.model.enums.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SessaoTest {

    @Test
    void testConstrutorComDatasValidas() {
        LocalDateTime dataInicio = LocalDateTime.now();
        LocalDateTime dataFim = dataInicio.plusMinutes(10);
        Pauta pauta = new Pauta();

        Sessao sessao = new Sessao(dataInicio, dataFim, pauta);

        assertEquals(dataInicio, sessao.getDataInicio(), "A data de início deve ser a mesma passada no construtor");
        assertEquals(dataFim, sessao.getDataFim(), "A data de fim deve ser a mesma passada no construtor");
        assertEquals(pauta, sessao.getPauta(), "A pauta deve ser a mesma passada no construtor");
    }

    @Test
    void testConstrutorComDataInicioNula() {
        LocalDateTime dataFim = LocalDateTime.now().plusMinutes(10);
        Pauta pauta = new Pauta();

        Sessao sessao = new Sessao(null, dataFim, pauta);

        assertNotNull(sessao.getDataInicio(), "A data de início não pode ser nula");
        assertTrue(sessao.getDataFim().isAfter(sessao.getDataInicio()), "A data de fim deve ser maior que a data de início");
    }

    @Test
    void testConstrutorComDataFimNula() {
        LocalDateTime dataInicio = LocalDateTime.now();
        Pauta pauta = new Pauta();

        Sessao sessao = new Sessao(dataInicio, null, pauta);

        assertNotNull(sessao.getDataFim(), "A data de fim não pode ser nula");
        assertTrue(sessao.getDataFim().isAfter(sessao.getDataInicio()), "A data de fim deve ser maior que a data de início");
    }

    @Test
    void testConstrutorComDataFimMenorQueDataInicio() {
        LocalDateTime dataInicio = LocalDateTime.now();
        LocalDateTime dataFim = dataInicio.minusMinutes(1);
        Pauta pauta = new Pauta();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Sessao(dataInicio, dataFim, pauta),
                "Deve lançar uma exceção quando a data de fim é menor que a data de início"
        );

        assertEquals("A data de fim deve ser maior que a data de início", exception.getMessage());
    }

    @Test
    void testGetStatusAberta() {
        LocalDateTime dataInicio = LocalDateTime.now().minusMinutes(1);
        LocalDateTime dataFim = LocalDateTime.now().plusMinutes(1);
        Pauta pauta = new Pauta();

        Sessao sessao = new Sessao(dataInicio, dataFim, pauta);

        assertEquals(Status.ABERTA, sessao.getStatus(), "O status deve ser ABERTA quando a sessão está em andamento");
    }

    @Test
    void testGetStatusFechada() {
        LocalDateTime dataInicio = LocalDateTime.now().minusMinutes(2);
        LocalDateTime dataFim = LocalDateTime.now().minusMinutes(1);
        Pauta pauta = new Pauta();

        Sessao sessao = new Sessao(dataInicio, dataFim, pauta);

        assertEquals(Status.FECHADA, sessao.getStatus(), "O status deve ser FECHADA quando a sessão já foi encerrada");
    }

    @Test
    void testIsOpen() {
        LocalDateTime dataInicio = LocalDateTime.now().minusMinutes(1);
        LocalDateTime dataFim = LocalDateTime.now().plusMinutes(1);
        Pauta pauta = new Pauta();

        Sessao sessao = new Sessao(dataInicio, dataFim, pauta);

        assertTrue(sessao.isOpen(), "A sessão deve estar aberta quando o status é ABERTA");
    }

    @Test
    void testIsFinished() {
        LocalDateTime dataInicio = LocalDateTime.now().minusMinutes(2);
        LocalDateTime dataFim = LocalDateTime.now().minusMinutes(1);
        Pauta pauta = new Pauta();

        Sessao sessao = new Sessao(dataInicio, dataFim, pauta);

        assertTrue(sessao.isFinished(), "A sessão deve estar encerrada quando a data atual é após a data de fim");
    }
}
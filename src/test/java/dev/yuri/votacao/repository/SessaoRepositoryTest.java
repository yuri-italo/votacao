package dev.yuri.votacao.repository;

import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Sessao;
import dev.yuri.votacao.model.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class SessaoRepositoryTest {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PautaRepository pautaRepository;  // Para criar uma Pauta para associar à Sessão

    @Test
    void testFindByPautaId_QuandoPautaExiste() {
        Pauta pauta = new Pauta();
        pauta.setNome("Pauta 1");
        pauta.setDescricao("Descrição da pauta");
        pautaRepository.save(pauta);

        Sessao sessao = new Sessao(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), pauta);
        sessaoRepository.save(sessao);

        Optional<Sessao> sessaoEncontrada = sessaoRepository.findByPautaId(pauta.getId());

        assertTrue(sessaoEncontrada.isPresent());
        assertEquals(pauta.getId(), sessaoEncontrada.get().getPauta().getId());
    }

    @Test
    void testFindByPautaId_QuandoPautaNaoExiste() {
        Long pautaId = 1L;

        Optional<Sessao> sessaoEncontrada = sessaoRepository.findByPautaId(pautaId);

        assertFalse(sessaoEncontrada.isPresent());
    }
}

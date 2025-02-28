package dev.yuri.votacao.repository;

import dev.yuri.votacao.model.Associado;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Voto;
import dev.yuri.votacao.model.enums.Escolha;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class VotoRepositoryTest {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Test
    void testExistsByPautaIdAndAssociadoId_QuandoVotoExistente() {
        Pauta pauta = new Pauta();
        pauta.setNome("Pauta 1");
        pauta.setDescricao("Descrição da pauta");
        pautaRepository.save(pauta);

        Associado associado = new Associado();
        associado.setNome("Associado 1");
        associado.setCpf("12345678901");
        associadoRepository.save(associado);

        Voto voto = new Voto();
        voto.setEscolha(Escolha.SIM);
        voto.setPauta(pauta);
        voto.setAssociado(associado);
        votoRepository.save(voto);

        boolean exists = votoRepository.existsByPautaIdAndAssociadoId(pauta.getId(), associado.getId());

        assertTrue(exists);
    }

    @Test
    void testExistsByPautaIdAndAssociadoId_QuandoVotoNaoExistente() {
        Long pautaId = 1L;
        Long associadoId = 1L;

        boolean exists = votoRepository.existsByPautaIdAndAssociadoId(pautaId, associadoId);

        assertFalse(exists);
    }

    @Test
    void testFindAllByPautaId_QuandoExistemVotos() {
        Pauta pauta = new Pauta();
        pauta.setNome("Pauta 2");
        pauta.setDescricao("Descrição da pauta");
        pautaRepository.save(pauta);

        Associado associado1 = new Associado();
        associado1.setNome("Associado 1");
        associado1.setCpf("12345678901");
        associadoRepository.save(associado1);

        Associado associado2 = new Associado();
        associado2.setNome("Associado 2");
        associado2.setCpf("23456789012");
        associadoRepository.save(associado2);

        Voto voto1 = new Voto();
        voto1.setEscolha(Escolha.SIM);
        voto1.setPauta(pauta);
        voto1.setAssociado(associado1);
        votoRepository.save(voto1);

        Voto voto2 = new Voto();
        voto2.setEscolha(Escolha.NAO);
        voto2.setPauta(pauta);
        voto2.setAssociado(associado2);
        votoRepository.save(voto2);

        List<Voto> votos = votoRepository.findAllByPautaId(pauta.getId());

        assertNotNull(votos);
        assertEquals(2, votos.size());
    }

    @Test
    void testFindAllByPautaId_QuandoNaoExistemVotos() {
        Long pautaId = 1L;

        List<Voto> votos = votoRepository.findAllByPautaId(pautaId);

        assertNotNull(votos);
        assertTrue(votos.isEmpty());
    }
}

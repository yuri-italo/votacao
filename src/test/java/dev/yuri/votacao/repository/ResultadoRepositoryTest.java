package dev.yuri.votacao.repository;

import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Resultado;
import dev.yuri.votacao.model.enums.Situacao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ResultadoRepositoryTest {

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByPautaId_QuandoResultadoExiste() {
        Pauta pauta = new Pauta();
        pauta.setNome("Pauta de Teste");
        pauta.setDescricao("Descrição da Pauta de Teste");
        entityManager.persist(pauta);

        Resultado resultado = new Resultado();
        resultado.setPauta(pauta);
        resultado.setQuantidadeSim(10L);
        resultado.setQuantidadeNao(5L);
        resultado.calcularSituacao();
        entityManager.persist(resultado);

        Optional<Resultado> foundResultado = resultadoRepository.findByPautaId(pauta.getId());

        assertTrue(foundResultado.isPresent());
        assertEquals(pauta.getId(), foundResultado.get().getPauta().getId());
        assertEquals(10L, foundResultado.get().getQuantidadeSim());
        assertEquals(5L, foundResultado.get().getQuantidadeNao());
        assertEquals(Situacao.APROVADA, foundResultado.get().getSituacao());
    }

    @Test
    void testFindByPautaId_QuandoResultadoNaoExiste() {
        Long pautaId = 999L;

        Optional<Resultado> foundResultado = resultadoRepository.findByPautaId(pautaId);

        assertFalse(foundResultado.isPresent());
    }
}
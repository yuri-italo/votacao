package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.SessionNotFinishedException;
import dev.yuri.votacao.model.*;
import dev.yuri.votacao.model.enums.Escolha;
import dev.yuri.votacao.model.enums.Situacao;
import dev.yuri.votacao.repository.ResultadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultadoServiceTest {

    @Mock
    private ResultadoRepository resultadoRepository;

    @Mock
    private PautaService pautaService;

    @Mock
    private SessaoService sessaoService;

    @Mock
    private VotoService votoService;

    @InjectMocks
    private ResultadoService resultadoService;

    private Pauta pauta;
    private Sessao sessao;
    private Voto votoSim;
    private Voto votoNao;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setNome("Reforma do Estacionamento");
        pauta.setDescricao("Discutir a reforma do estacionamento para melhorar a segurança.");

        sessao = new Sessao();
        sessao.setId(1L);
        sessao.setPauta(pauta);
        sessao.setDataInicio(LocalDateTime.now().minusMinutes(10));
        sessao.setDataFim(LocalDateTime.now().minusMinutes(1));

        votoSim = new Voto();
        votoSim.setId(1L);
        votoSim.setEscolha(Escolha.SIM);
        votoSim.setPauta(pauta);

        votoNao = new Voto();
        votoNao.setId(2L);
        votoNao.setEscolha(Escolha.NAO);
        votoNao.setPauta(pauta);
    }

    @Test
    void testCreate() {
        Map<Escolha, Long> contagemDosVotos = Map.of(Escolha.SIM, 2L, Escolha.NAO, 1L);
        Resultado resultadoSalvo = new Resultado();
        resultadoSalvo.setId(1L);
        resultadoSalvo.setPauta(pauta);
        resultadoSalvo.setQuantidadeSim(2L);
        resultadoSalvo.setQuantidadeNao(1L);
        resultadoSalvo.setSituacao(Situacao.APROVADA);

        when(resultadoRepository.save(any(Resultado.class))).thenReturn(resultadoSalvo);

        Resultado resultado = resultadoService.create(pauta, contagemDosVotos);

        assertNotNull(resultado, "O resultado não deve ser nulo");
        assertEquals(1L, resultado.getId(), "O ID do resultado deve ser 1");
        assertEquals(2L, resultado.getQuantidadeSim(), "A quantidade de votos 'Sim' deve ser 2");
        assertEquals(1L, resultado.getQuantidadeNao(), "A quantidade de votos 'Não' deve ser 1");
        assertEquals(Situacao.APROVADA, resultado.getSituacao(), "A situação deve ser APROVADA");

        verify(resultadoRepository, times(1)).save(any(Resultado.class));
    }

    @Test
    void testGetResultadoExistente() {
        Resultado resultadoExistente = new Resultado();
        resultadoExistente.setId(1L);
        resultadoExistente.setPauta(pauta);
        resultadoExistente.setQuantidadeSim(2L);
        resultadoExistente.setQuantidadeNao(1L);
        resultadoExistente.setSituacao(Situacao.APROVADA);

        when(resultadoRepository.findByPautaId(1L)).thenReturn(Optional.of(resultadoExistente));

        Resultado resultado = resultadoService.getResultado(1L);

        assertNotNull(resultado, "O resultado não deve ser nulo");
        assertEquals(1L, resultado.getId(), "O ID do resultado deve ser 1");
        assertEquals(2L, resultado.getQuantidadeSim(), "A quantidade de votos 'Sim' deve ser 2");
        assertEquals(1L, resultado.getQuantidadeNao(), "A quantidade de votos 'Não' deve ser 1");
        assertEquals(Situacao.APROVADA, resultado.getSituacao(), "A situação deve ser APROVADA");

        verify(resultadoRepository, times(1)).findByPautaId(1L);
        verify(pautaService, never()).findById(any());
        verify(sessaoService, never()).findByPautaId(any());
        verify(votoService, never()).findAllByPautaId(any());
    }

    @Test
    void testGetResultadoNaoExistente() {
        when(resultadoRepository.findByPautaId(1L)).thenReturn(Optional.empty());
        when(pautaService.findById(1L)).thenReturn(pauta);
        when(sessaoService.findByPautaId(1L)).thenReturn(sessao);
        when(votoService.findAllByPautaId(1L)).thenReturn(List.of(votoSim, votoSim, votoNao));

        Resultado resultadoSalvo = new Resultado();
        resultadoSalvo.setId(1L);
        resultadoSalvo.setPauta(pauta);
        resultadoSalvo.setQuantidadeSim(2L);
        resultadoSalvo.setQuantidadeNao(1L);
        resultadoSalvo.setSituacao(Situacao.APROVADA);

        when(resultadoRepository.save(any(Resultado.class))).thenReturn(resultadoSalvo);

        Resultado resultado = resultadoService.getResultado(1L);

        assertNotNull(resultado, "O resultado não deve ser nulo");
        assertEquals(1L, resultado.getId(), "O ID do resultado deve ser 1");
        assertEquals(2L, resultado.getQuantidadeSim(), "A quantidade de votos 'Sim' deve ser 2");
        assertEquals(1L, resultado.getQuantidadeNao(), "A quantidade de votos 'Não' deve ser 1");
        assertEquals(Situacao.APROVADA, resultado.getSituacao(), "A situação deve ser APROVADA");

        verify(resultadoRepository, times(1)).findByPautaId(1L);
        verify(pautaService, times(1)).findById(1L);
        verify(sessaoService, times(1)).findByPautaId(1L);
        verify(votoService, times(1)).findAllByPautaId(1L);
        verify(resultadoRepository, times(1)).save(any(Resultado.class));
    }

    @Test
    void testCalcularResultadoComSessaoNaoEncerrada() {
        sessao.setDataFim(LocalDateTime.now().plusMinutes(10));
        when(pautaService.findById(1L)).thenReturn(pauta);
        when(sessaoService.findByPautaId(1L)).thenReturn(sessao);

        SessionNotFinishedException exception = assertThrows(
                SessionNotFinishedException.class,
                () -> resultadoService.getResultado(1L),
                "Deve lançar SessionNotFinishedException quando a sessão não estiver encerrada"
        );

        assertEquals("O resultado da votação só pode ser consultado após o encerramento da sessão.", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(resultadoRepository, times(1)).findByPautaId(1L);
        verify(pautaService, times(1)).findById(1L);
        verify(sessaoService, times(1)).findByPautaId(1L);
        verify(votoService, never()).findAllByPautaId(any());
        verify(resultadoRepository, never()).save(any());
    }
}
package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Sessao;
import dev.yuri.votacao.repository.SessaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @InjectMocks
    private SessaoService sessaoService;

    private Sessao sessao;
    private Pauta pauta;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setNome("Reforma do Estacionamento");
        pauta.setDescricao("Discutir a reforma do estacionamento para melhorar a segurança.");

        sessao = new Sessao();
        sessao.setId(1L);
        sessao.setPauta(pauta);
        sessao.setDataInicio(LocalDateTime.now());
        sessao.setDataFim(LocalDateTime.now().plusMinutes(10));
    }

    @Test
    void testSaveSuccess() {
        when(sessaoRepository.findByPautaId(1L)).thenReturn(Optional.empty());
        when(sessaoRepository.save(sessao)).thenReturn(sessao);

        Sessao result = sessaoService.save(sessao);

        assertNotNull(result, "A sessão não deve ser nula");
        assertEquals(1L, result.getId(), "O ID da sessão deve ser 1");
        assertEquals(pauta, result.getPauta(), "A pauta da sessão deve ser a esperada");

        verify(sessaoRepository, times(1)).findByPautaId(1L);
        verify(sessaoRepository, times(1)).save(sessao);
    }

    @Test
    void testSaveWithExistingSessao() {
        when(sessaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessao));

        EntityAlreadyExistsException exception = assertThrows(
                EntityAlreadyExistsException.class,
                () -> sessaoService.save(sessao),
                "Deve lançar EntityAlreadyExistsException quando já existir uma sessão para a pauta"
        );

        assertEquals("Já existe uma sessão para a pauta com ID: 1", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(sessaoRepository, times(1)).findByPautaId(1L);
        verify(sessaoRepository, never()).save(any());
    }

    @Test
    void testFindByIdSuccess() {
        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessao));

        Sessao result = sessaoService.findById(1L);

        assertNotNull(result, "A sessão não deve ser nula");
        assertEquals(1L, result.getId(), "O ID da sessão deve ser 1");
        assertEquals(pauta, result.getPauta(), "A pauta da sessão deve ser a esperada");

        verify(sessaoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(sessaoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> sessaoService.findById(1L),
                "Deve lançar EntityNotFoundException quando a sessão não for encontrada"
        );

        assertEquals("Sessão não encontrada com ID: 1", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(sessaoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByPautaIdSuccess() {
        when(sessaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessao));

        Sessao result = sessaoService.findByPautaId(1L);

        assertNotNull(result, "A sessão não deve ser nula");
        assertEquals(1L, result.getId(), "O ID da sessão deve ser 1");
        assertEquals(pauta, result.getPauta(), "A pauta da sessão deve ser a esperada");

        verify(sessaoRepository, times(1)).findByPautaId(1L);
    }

    @Test
    void testFindByPautaIdNotFound() {
        when(sessaoRepository.findByPautaId(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> sessaoService.findByPautaId(1L),
                "Deve lançar EntityNotFoundException quando a sessão não for encontrada"
        );

        assertEquals("Sessão não encontrada para a pauta com ID: 1", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(sessaoRepository, times(1)).findByPautaId(1L);
    }
}
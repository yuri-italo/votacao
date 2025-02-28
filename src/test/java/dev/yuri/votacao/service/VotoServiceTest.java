package dev.yuri.votacao.service;

import dev.yuri.votacao.client.CpfValidationClient;
import dev.yuri.votacao.client.enums.Status;
import dev.yuri.votacao.client.exception.InvalidCpfException;
import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.SessionClosedException;
import dev.yuri.votacao.model.Associado;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Sessao;
import dev.yuri.votacao.model.Voto;
import dev.yuri.votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoService sessaoService;

    @Mock
    private CpfValidationClient cpfValidationClient;

    @InjectMocks
    private VotoService votoService;

    private Voto voto;
    private Associado associado;
    private Pauta pauta;
    private Sessao sessao;

    @BeforeEach
    void setUp() {
        associado = new Associado();
        associado.setId(1L);
        associado.setNome("João Silva");
        associado.setCpf("12345678901");

        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setNome("Reforma do Estacionamento");
        pauta.setDescricao("Discutir a reforma do estacionamento para melhorar a segurança.");

        sessao = new Sessao();
        sessao.setId(1L);
        sessao.setPauta(pauta);
        sessao.setDataInicio(LocalDateTime.now().minusMinutes(10));
        sessao.setDataFim(LocalDateTime.now().plusMinutes(10));

        voto = new Voto();
        voto.setId(1L);
        voto.setAssociado(associado);
        voto.setPauta(pauta);
        voto.setEscolha(dev.yuri.votacao.model.enums.Escolha.SIM);
    }

    @Test
    void testSaveSuccess() {
        when(cpfValidationClient.validarCpf("12345678901")).thenReturn(Status.ABLE_TO_VOTE);
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, 1L)).thenReturn(false);
        when(sessaoService.findByPautaId(1L)).thenReturn(sessao);
        when(votoRepository.save(voto)).thenReturn(voto);

        Voto result = votoService.save(voto);

        assertNotNull(result, "O voto não deve ser nulo");
        assertEquals(1L, result.getId(), "O ID do voto deve ser 1");
        assertEquals(associado, result.getAssociado(), "O associado do voto deve ser o esperado");
        assertEquals(pauta, result.getPauta(), "A pauta do voto deve ser a esperada");

        verify(cpfValidationClient, times(1)).validarCpf("12345678901");
        verify(votoRepository, times(1)).existsByPautaIdAndAssociadoId(1L, 1L);
        verify(sessaoService, times(1)).findByPautaId(1L);
        verify(votoRepository, times(1)).save(voto);
    }

    @Test
    void testSaveWithInvalidCpf() {
        when(cpfValidationClient.validarCpf("12345678901")).thenReturn(Status.UNABLE_TO_VOTE);

        InvalidCpfException exception = assertThrows(
                InvalidCpfException.class,
                () -> votoService.save(voto),
                "Deve lançar InvalidCpfException quando o CPF não for válido"
        );

        assertEquals("CPF do associado não está apto a votar", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(cpfValidationClient, times(1)).validarCpf("12345678901");
        verify(votoRepository, never()).existsByPautaIdAndAssociadoId(any(), any());
        verify(sessaoService, never()).findByPautaId(any());
        verify(votoRepository, never()).save(any());
    }

    @Test
    void testSaveWithExistingVoto() {
        when(cpfValidationClient.validarCpf("12345678901")).thenReturn(Status.ABLE_TO_VOTE);
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, 1L)).thenReturn(true);

        EntityAlreadyExistsException exception = assertThrows(
                EntityAlreadyExistsException.class,
                () -> votoService.save(voto),
                "Deve lançar EntityAlreadyExistsException quando o associado já tiver votado na pauta"
        );

        assertEquals("Associado já votou nesta pauta", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(cpfValidationClient, times(1)).validarCpf("12345678901");
        verify(votoRepository, times(1)).existsByPautaIdAndAssociadoId(1L, 1L);
        verify(sessaoService, never()).findByPautaId(any());
        verify(votoRepository, never()).save(any());
    }

    @Test
    void testSaveWithClosedSession() {
        sessao.setDataFim(LocalDateTime.now().minusMinutes(1));
        when(cpfValidationClient.validarCpf("12345678901")).thenReturn(Status.ABLE_TO_VOTE);
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, 1L)).thenReturn(false);
        when(sessaoService.findByPautaId(1L)).thenReturn(sessao);

        SessionClosedException exception = assertThrows(
                SessionClosedException.class,
                () -> votoService.save(voto),
                "Deve lançar SessionClosedException quando a sessão estiver fechada"
        );

        assertEquals("A sessão de votação está fechada", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(cpfValidationClient, times(1)).validarCpf("12345678901");
        verify(votoRepository, times(1)).existsByPautaIdAndAssociadoId(1L, 1L);
        verify(sessaoService, times(1)).findByPautaId(1L);
        verify(votoRepository, never()).save(any());
    }

    @Test
    void testSaveWithNullPauta() {
        voto.setPauta(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> votoService.save(voto),
                "Deve lançar IllegalArgumentException quando a pauta for nula"
        );

        assertEquals("Pauta não pode ser nula", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(cpfValidationClient, never()).validarCpf(any());
        verify(votoRepository, never()).existsByPautaIdAndAssociadoId(any(), any());
        verify(sessaoService, never()).findByPautaId(any());
        verify(votoRepository, never()).save(any());
    }

    @Test
    void testSaveWithNullAssociado() {
        voto.setAssociado(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> votoService.save(voto),
                "Deve lançar IllegalArgumentException quando o associado for nulo"
        );

        assertEquals("Associado não pode ser nulo", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(cpfValidationClient, never()).validarCpf(any());
        verify(votoRepository, never()).existsByPautaIdAndAssociadoId(any(), any());
        verify(sessaoService, never()).findByPautaId(any());
        verify(votoRepository, never()).save(any());
    }
}
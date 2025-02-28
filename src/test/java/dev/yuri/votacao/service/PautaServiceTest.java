package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    private Pauta pauta;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setNome("Reforma do Estacionamento");
        pauta.setDescricao("Discutir a reforma do estacionamento para melhorar a segurança.");
    }

    @Test
    void testSaveSuccess() {
        when(pautaRepository.save(pauta)).thenReturn(pauta);

        Pauta result = pautaService.save(pauta);

        assertNotNull(result, "A pauta não deve ser nula");
        assertEquals(1L, result.getId(), "O ID da pauta deve ser 1");
        assertEquals("Reforma do Estacionamento", result.getNome(), "O nome da pauta deve ser 'Reforma do Estacionamento'");
        assertEquals("Discutir a reforma do estacionamento para melhorar a segurança.", result.getDescricao(), "A descrição da pauta deve ser a esperada");

        verify(pautaRepository, times(1)).save(pauta);
    }

    @Test
    void testFindByIdSuccess() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        Pauta result = pautaService.findById(1L);

        assertNotNull(result, "A pauta não deve ser nula");
        assertEquals(1L, result.getId(), "O ID da pauta deve ser 1");
        assertEquals("Reforma do Estacionamento", result.getNome(), "O nome da pauta deve ser 'Reforma do Estacionamento'");
        assertEquals("Discutir a reforma do estacionamento para melhorar a segurança.", result.getDescricao(), "A descrição da pauta deve ser a esperada");

        verify(pautaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> pautaService.findById(1L),
                "Deve lançar EntityNotFoundException quando a pauta não for encontrada"
        );

        assertEquals("Pauta não encontrada com ID: 1", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(pautaRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSuccess() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        doNothing().when(pautaRepository).delete(pauta);

        pautaService.delete(1L);

        verify(pautaRepository, times(1)).findById(1L);
        verify(pautaRepository, times(1)).delete(pauta);
    }

    @Test
    void testDeleteNotFound() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> pautaService.delete(1L),
                "Deve lançar EntityNotFoundException quando a pauta não for encontrada"
        );

        assertEquals("Pauta não encontrada com ID: 1", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(pautaRepository, times(1)).findById(1L);
        verify(pautaRepository, never()).delete(any());
    }
}
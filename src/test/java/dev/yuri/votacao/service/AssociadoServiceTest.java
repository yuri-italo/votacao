package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Associado;
import dev.yuri.votacao.repository.AssociadoRepository;
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
class AssociadoServiceTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private AssociadoService associadoService;

    private Associado associado;

    @BeforeEach
    void setUp() {
        associado = new Associado();
        associado.setId(1L);
        associado.setNome("João Silva");
        associado.setCpf("12345678901");
    }

    @Test
    void testFindByIdSuccess() {
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));

        Associado result = associadoService.findById(1L);

        assertNotNull(result, "O associado não deve ser nulo");
        assertEquals(1L, result.getId(), "O ID do associado deve ser 1");
        assertEquals("João Silva", result.getNome(), "O nome do associado deve ser 'João Silva'");
        assertEquals("12345678901", result.getCpf(), "O CPF do associado deve ser '12345678901'");

        verify(associadoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(associadoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> associadoService.findById(1L),
                "Deve lançar EntityNotFoundException quando o associado não for encontrado"
        );

        assertEquals("Associado não encontrado com o ID: 1", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(associadoRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveSuccess() {
        when(associadoRepository.existsByCpf("12345678901")).thenReturn(false);
        when(associadoRepository.save(associado)).thenReturn(associado);

        Associado result = associadoService.save(associado);

        assertNotNull(result, "O associado não deve ser nulo");
        assertEquals(1L, result.getId(), "O ID do associado deve ser 1");
        assertEquals("João Silva", result.getNome(), "O nome do associado deve ser 'João Silva'");
        assertEquals("12345678901", result.getCpf(), "O CPF do associado deve ser '12345678901'");

        verify(associadoRepository, times(1)).existsByCpf("12345678901");
        verify(associadoRepository, times(1)).save(associado);
    }

    @Test
    void testSaveWithExistingCpf() {
        when(associadoRepository.existsByCpf("12345678901")).thenReturn(true);

        EntityAlreadyExistsException exception = assertThrows(
                EntityAlreadyExistsException.class,
                () -> associadoService.save(associado),
                "Deve lançar EntityAlreadyExistsException quando o CPF já existir"
        );

        assertEquals("Já existe um associado com este CPF", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(associadoRepository, times(1)).existsByCpf("12345678901");
        verify(associadoRepository, never()).save(any());
    }

    @Test
    void testDeleteSuccess() {
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
        doNothing().when(associadoRepository).delete(associado);

        associadoService.delete(1L);

        verify(associadoRepository, times(1)).findById(1L);
        verify(associadoRepository, times(1)).delete(associado);
    }

    @Test
    void testDeleteNotFound() {
        when(associadoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> associadoService.delete(1L),
                "Deve lançar EntityNotFoundException quando o associado não for encontrado"
        );

        assertEquals("Associado não encontrado com o ID: 1", exception.getMessage(), "A mensagem da exceção deve ser a esperada");

        verify(associadoRepository, times(1)).findById(1L);
        verify(associadoRepository, never()).delete(any());
    }
}
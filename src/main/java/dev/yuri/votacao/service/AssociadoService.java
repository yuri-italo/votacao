package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Associado;
import dev.yuri.votacao.repository.AssociadoRepository;
import org.springframework.stereotype.Service;

@Service
public class AssociadoService {
    private final AssociadoRepository associadoRepository;

    public AssociadoService(AssociadoRepository associadoRepository) {
        this.associadoRepository = associadoRepository;
    }

    public Associado findById(Long id) {
        return associadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Associado não encontrado com o ID: " + id));
    }

    public Associado save(Associado associado) {
        if (associadoRepository.existsByCpf(associado.getCpf())) {
            throw new EntityAlreadyExistsException("Já existe um associado com este CPF");
        }

        return associadoRepository.save(associado);
    }

    public void delete(Long id) {
        var associado = findById(id);
        associadoRepository.delete(associado);
    }
}

package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Associado;
import dev.yuri.votacao.repository.AssociadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AssociadoService {
    private static final Logger log = LoggerFactory.getLogger(AssociadoService.class);

    private final AssociadoRepository associadoRepository;

    public AssociadoService(AssociadoRepository associadoRepository) {
        this.associadoRepository = associadoRepository;
    }

    public Associado findById(Long id) {
        log.info("Buscando associado com ID: {}", id);
        return associadoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Associado não encontrado com o ID: {}", id);
                    return new EntityNotFoundException("Associado não encontrado com o ID: " + id);
                });
    }

    public Associado save(Associado associado) {
        log.info("Tentando salvar associado: {}", associado);
        if (associadoRepository.existsByCpf(associado.getCpf())) {
            log.error("Já existe um associado com este CPF");
            throw new EntityAlreadyExistsException("Já existe um associado com este CPF");
        }

        log.info("Associado salvo com sucesso: {}", associado);
        return associadoRepository.save(associado);
    }

    public void delete(Long id) {
        log.info("Tentando excluir associado com ID: {}", id);
        var associado = findById(id);
        associadoRepository.delete(associado);
        log.info("Associado com ID: {} excluído com sucesso", id);
    }
}

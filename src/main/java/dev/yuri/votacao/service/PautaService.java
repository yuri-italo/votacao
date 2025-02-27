package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.repository.PautaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PautaService {
    private static final Logger log = LoggerFactory.getLogger(PautaService.class);

    private final PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Pauta save(Pauta pauta) {
        log.info("Salvando pauta: {}", pauta);
        return this.pautaRepository.save(pauta);
    }

    public Pauta findById(Long id) {
        log.info("Buscando pauta com ID: {}", id);
        return pautaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pauta não encontrada com ID: {}", id);
                    return new EntityNotFoundException("Pauta não encontrada com ID: " + id);
                });
    }

    public void delete(Long id) {
        log.info("Tentando excluir pauta com ID: {}", id);
        var pauta = findById(id);
        pautaRepository.delete(pauta);
        log.info("Pauta com ID: {} excluída com sucesso", id);
    }
}

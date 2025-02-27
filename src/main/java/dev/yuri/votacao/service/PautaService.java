package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.repository.PautaRepository;
import org.springframework.stereotype.Service;

@Service
public class PautaService {
    private final PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Pauta save(Pauta pauta) {
        return this.pautaRepository.save(pauta);
    }

    public Pauta findById(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Pauta não encontrada com ID: " + id));
    }

    public void delete(Long id) {
        var pauta = findById(id);
        pautaRepository.delete(pauta);
    }
}

package dev.yuri.votacao.service;

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
}

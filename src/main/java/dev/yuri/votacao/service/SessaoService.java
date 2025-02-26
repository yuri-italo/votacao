package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.model.Sessao;
import dev.yuri.votacao.repository.SessaoRepository;
import org.springframework.stereotype.Service;

@Service
public class SessaoService {
    private final SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    public Sessao save(Sessao sessao) {
        if (sessaoRepository.existsByPautaId(sessao.getPauta().getId())) {
            throw new EntityAlreadyExistsException("Já existe uma sessão para esta pauta.");
        }
        return this.sessaoRepository.save(sessao);
    }

}

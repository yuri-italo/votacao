package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Sessao;
import dev.yuri.votacao.repository.SessaoRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SessaoService {
    private final SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    public Sessao save(Sessao sessao) {
        Long pautaId = Objects.requireNonNull(sessao.getPauta().getId(),
                "O ID da pauta não pode ser nulo");

        sessaoRepository.findByPautaId(pautaId).ifPresent(s -> {
            throw new EntityAlreadyExistsException("Já existe uma sessão para a pauta com ID: " + pautaId);
        });

        return sessaoRepository.save(sessao);
    }

    public Sessao findById(Long id) {
        return sessaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sessão não encontrada com ID: " + id));
    }

    public Sessao findByPautaId(Long id) {
        return sessaoRepository.findByPautaId(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sessão não encontrada para a pauta com ID: " + id));
    }
}

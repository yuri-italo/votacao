package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Sessao;
import dev.yuri.votacao.repository.SessaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SessaoService {
    private static final Logger log = LoggerFactory.getLogger(SessaoService.class);

    private final SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    public Sessao save(Sessao sessao) {
        Long pautaId = Objects.requireNonNull(sessao.getPauta().getId(),
                "O ID da pauta não pode ser nulo");

        log.info("Tentando salvar sessão para a pauta ID: {}", pautaId);
        sessaoRepository.findByPautaId(pautaId).ifPresent(s -> {
            throw new EntityAlreadyExistsException("Já existe uma sessão para a pauta com ID: " + pautaId);
        });

        return sessaoRepository.save(sessao);
    }

    public Sessao findById(Long id) {
        log.info("Buscando sessão com ID: {}", id);
        return sessaoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Sessão não encontrada com ID: {}", id);
                    return new EntityNotFoundException("Sessão não encontrada com ID: " + id);
                });
    }

    public Sessao findByPautaId(Long id) {
        log.info("Buscando sessão para a pauta ID: {}", id);
        return sessaoRepository.findByPautaId(id)
                .orElseThrow(() -> {
                    log.warn("Sessão não encontrada para a pauta ID: {}", id);
                    return new EntityNotFoundException("Sessão não encontrada para a pauta com ID: " + id);
                });
    }
}

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

    /**
     * Salva uma nova sessão de votação para uma pauta. Se já existir uma sessão para a pauta,
     * uma exceção será lançada.
     *
     * @param sessao A sessão a ser salva.
     * @return A sessão salva.
     * @throws EntityAlreadyExistsException Se já existir uma sessão para a pauta.
     */
    public Sessao save(Sessao sessao) {
        Long pautaId = Objects.requireNonNull(sessao.getPauta().getId(),
                "O ID da pauta não pode ser nulo");

        log.info("Tentando salvar sessão para a pauta ID: {}", pautaId);
        sessaoRepository.findByPautaId(pautaId).ifPresent(s -> {
            throw new EntityAlreadyExistsException("Já existe uma sessão para a pauta com ID: " + pautaId);
        });

        return sessaoRepository.save(sessao);
    }

    /**
     * Recupera uma sessão de votação pelo seu ID.
     *
     * @param id O ID da sessão.
     * @return A sessão encontrada.
     * @throws EntityNotFoundException Se a sessão não for encontrada.
     */
    public Sessao findById(Long id) {
        log.info("Buscando sessão com ID: {}", id);
        return sessaoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Sessão não encontrada com ID: {}", id);
                    return new EntityNotFoundException("Sessão não encontrada com ID: " + id);
                });
    }

    /**
     * Recupera a sessão de votação associada a uma pauta pelo ID da pauta.
     *
     * @param id O ID da pauta.
     * @return A sessão associada à pauta.
     * @throws EntityNotFoundException Se a sessão não for encontrada para a pauta.
     */
    public Sessao findByPautaId(Long id) {
        log.info("Buscando sessão para a pauta ID: {}", id);
        return sessaoRepository.findByPautaId(id)
                .orElseThrow(() -> {
                    log.warn("Sessão não encontrada para a pauta ID: {}", id);
                    return new EntityNotFoundException("Sessão não encontrada para a pauta com ID: " + id);
                });
    }
}

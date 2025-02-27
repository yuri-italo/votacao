package dev.yuri.votacao.service;

import dev.yuri.votacao.client.CpfValidationClient;
import dev.yuri.votacao.client.enums.Status;
import dev.yuri.votacao.client.exception.InvalidCpfException;
import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.SessionClosedException;
import dev.yuri.votacao.model.Voto;
import dev.yuri.votacao.repository.VotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VotoService {
    private static final Logger log = LoggerFactory.getLogger(VotoService.class);

    private final VotoRepository votoRepository;
    private final SessaoService sessaoService;
    private final CpfValidationClient cpfValidationClient;

    public VotoService(VotoRepository votoRepository, SessaoService sessaoService, CpfValidationClient cpfValidationClient) {
        this.votoRepository = votoRepository;
        this.sessaoService = sessaoService;
        this.cpfValidationClient = cpfValidationClient;
    }

    public Voto save(Voto voto) {
        log.info("Tentando salvar voto do associado ID: {} na pauta ID: {}",
                voto.getAssociado().getId(), voto.getPauta().getId());
        validarVoto(voto);
        return votoRepository.save(voto);
    }

    public List<Voto> findAllByPautaId(Long pautaId) {
        log.info("Buscando votos para a pauta ID: {}", pautaId);
        return votoRepository.findAllByPautaId(pautaId);
    }

    private void validarVoto(Voto voto) {
        var pauta = Optional.ofNullable(voto.getPauta())
                .orElseThrow(() -> {
                    log.warn("Tentativa de voto com pauta nula.");
                    return new IllegalArgumentException("Pauta não pode ser nula");
                });
        var associado = Optional.ofNullable(voto.getAssociado())
                .orElseThrow(() -> {
                    log.warn("Tentativa de voto com associado nulo.");
                    return new IllegalArgumentException("Associado não pode ser nulo");
                });

        log.info("Validando CPF do associado ID: {}", associado.getId());
        var status = cpfValidationClient.validarCpf(associado.getCpf());
        if (Status.UNABLE_TO_VOTE.equals(status)) {
            log.warn("CPF do associado ID: {} não está apto a votar.", associado.getId());
            throw new InvalidCpfException("CPF do associado não está apto a votar");
        }

        if (votoRepository.existsByPautaIdAndAssociadoId(pauta.getId(), associado.getId())) {
            log.warn("Associado ID: {} já votou na pauta ID: {}", associado.getId(), pauta.getId());
            throw new EntityAlreadyExistsException("Associado já votou nesta pauta");
        }

        var sessao = sessaoService.findByPautaId(pauta.getId());
        if (!sessao.isOpen()) {
            log.warn("Tentativa de voto na pauta ID: {}, mas a sessão está fechada.", pauta.getId());
            throw new SessionClosedException("A sessão de votação está fechada");
        }
    }

}

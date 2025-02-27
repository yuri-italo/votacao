package dev.yuri.votacao.service;

import dev.yuri.votacao.client.CpfValidationClient;
import dev.yuri.votacao.client.enums.Status;
import dev.yuri.votacao.client.exception.InvalidCpfException;
import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.SessionClosedException;
import dev.yuri.votacao.model.Voto;
import dev.yuri.votacao.repository.VotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VotoService {
    private final VotoRepository votoRepository;
    private final SessaoService sessaoService;
    private final CpfValidationClient cpfValidationClient;

    public VotoService(VotoRepository votoRepository, SessaoService sessaoService, CpfValidationClient cpfValidationClient) {
        this.votoRepository = votoRepository;
        this.sessaoService = sessaoService;
        this.cpfValidationClient = cpfValidationClient;
    }

    public Voto save(Voto voto) {
        validarVoto(voto);
        return votoRepository.save(voto);
    }

    public List<Voto> findAllByPautaId(Long pautaId) {
        return votoRepository.findAllByPautaId(pautaId);
    }

    private void validarVoto(Voto voto) {
        var pauta = Optional.ofNullable(voto.getPauta())
                .orElseThrow(() -> new IllegalArgumentException("Pauta não pode ser nula"));
        var associado = Optional.ofNullable(voto.getAssociado())
                .orElseThrow(() -> new IllegalArgumentException("Associado não pode ser nulo"));

        var status = cpfValidationClient.validarCpf(associado.getCpf());
        if (Status.UNABLE_TO_VOTE.equals(status)) {
            throw new InvalidCpfException("CPF do associado não está apto a votar");
        }

        if (votoRepository.existsByPautaIdAndAssociadoId(pauta.getId(), associado.getId())) {
            throw new EntityAlreadyExistsException("Associado já votou nesta pauta");
        }

        var sessao = sessaoService.findByPautaId(pauta.getId());
        if (!sessao.isOpen()) {
            throw new SessionClosedException("A sessão de votação está fechada");
        }
    }

}

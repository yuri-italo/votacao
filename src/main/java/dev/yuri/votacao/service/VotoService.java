package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.SessionClosedException;
import dev.yuri.votacao.model.Sessao;
import dev.yuri.votacao.model.Voto;
import dev.yuri.votacao.model.enums.Status;
import dev.yuri.votacao.repository.VotoRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class VotoService {
    private final VotoRepository votoRepository;
    private final SessaoService sessaoService;

    public VotoService(VotoRepository votoRepository, SessaoService sessaoService) {
        this.votoRepository = votoRepository;
        this.sessaoService = sessaoService;
    }

    public Voto save(Voto voto) {
        validarVoto(voto);
        return votoRepository.save(voto);
    }

    private void validarVoto(Voto voto) {
        Long pautaId = Objects.requireNonNull(voto.getPauta().getId(), "Pauta não pode ser nula");
        Long associadoId = Objects.requireNonNull(voto.getAssociado().getId(), "Associado não pode ser nulo");

        if (votoRepository.existsByPautaIdAndAssociadoId(pautaId, associadoId)) {
            throw new EntityAlreadyExistsException("Associado já votou nesta pauta");
        }

        var sessao = sessaoService.findByPautaId(pautaId);
        if (!sessao.isOpen()) {
            throw new SessionClosedException("A sessão de votação está fechada");
        }
    }
}

package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.EntityAlreadyExistsException;
import dev.yuri.votacao.exception.SessionClosedException;
import dev.yuri.votacao.exception.SessionNotFinishedException;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Resultado;
import dev.yuri.votacao.model.Sessao;
import dev.yuri.votacao.model.Voto;
import dev.yuri.votacao.model.enums.Escolha;
import dev.yuri.votacao.repository.VotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VotoService {
    private final VotoRepository votoRepository;
    private final SessaoService sessaoService;
    private final PautaService pautaService;
    private final ResultadoService resultadoService;

    public VotoService(VotoRepository votoRepository, SessaoService sessaoService, PautaService pautaService, ResultadoService resultadoService) {
        this.votoRepository = votoRepository;
        this.sessaoService = sessaoService;
        this.pautaService = pautaService;
        this.resultadoService = resultadoService;
    }

    public Voto save(Voto voto) {
        validarVoto(voto);
        return votoRepository.save(voto);
    }

    public Resultado getResultado(Long pautaId) {
        Objects.requireNonNull(pautaId, "O ID da pauta não pode ser nulo");

        return resultadoService.getByPautaId(pautaId)
                .orElseGet(() -> calcularResultado(pautaId));
    }

    private Resultado calcularResultado(Long pautaId) {
        Pauta pauta = pautaService.findById(pautaId);
        Sessao sessao = sessaoService.findByPautaId(pautaId);

        if (!sessao.isFinished()) {
            throw new SessionNotFinishedException("O resultado da votação só pode ser consultado após o encerramento da sessão.");
        }

        List<Voto> votosPorPauta = votoRepository.findAllByPautaId(pautaId);
        Map<Escolha, Long> contagemDosVotos = votosPorPauta.stream()
                .collect(Collectors.groupingBy(Voto::getEscolha, Collectors.counting()));

        return resultadoService.create(pauta, contagemDosVotos);
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

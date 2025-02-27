package dev.yuri.votacao.service;

import dev.yuri.votacao.exception.SessionNotFinishedException;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Resultado;
import dev.yuri.votacao.model.Sessao;
import dev.yuri.votacao.model.Voto;
import dev.yuri.votacao.model.enums.Escolha;
import dev.yuri.votacao.repository.ResultadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ResultadoService {
    private static final Logger log = LoggerFactory.getLogger(ResultadoService.class);

    private final ResultadoRepository resultadoRepository;
    private final PautaService pautaService;
    private final SessaoService sessaoService;
    private final VotoService votoService;

    public ResultadoService(ResultadoRepository resultadoRepository, PautaService pautaService, SessaoService sessaoService, VotoService votoService) {
        this.resultadoRepository = resultadoRepository;
        this.pautaService = pautaService;
        this.sessaoService = sessaoService;
        this.votoService = votoService;
    }

    public Resultado create(Pauta pauta, Map<Escolha, Long> contagemDosVotos) {
        log.info("Criando resultado para a pauta ID: {}", pauta.getId());
        Resultado resultado = new Resultado();
        resultado.setPauta(pauta);
        resultado.setQuantidadeSim(contagemDosVotos.getOrDefault(Escolha.SIM, 0L));
        resultado.setQuantidadeNao(contagemDosVotos.getOrDefault(Escolha.NAO, 0L));
        resultado.calcularSituacao();

        return resultadoRepository.save(resultado);
    }

    public Resultado getResultado(Long pautaId) {
        Objects.requireNonNull(pautaId, "O ID da pauta não pode ser nulo");
        log.info("Buscando resultado para a pauta ID: {}", pautaId);

        return resultadoRepository.findByPautaId(pautaId)
                .orElseGet(() -> {
                    log.warn("Nenhum resultado encontrado para a pauta ID: {}. Calculando resultado...", pautaId);
                    return calcularResultado(pautaId);
                });
    }

    private Resultado calcularResultado(Long pautaId) {
        log.info("Iniciando cálculo do resultado para a pauta ID: {}", pautaId);
        Pauta pauta = pautaService.findById(pautaId);
        Sessao sessao = sessaoService.findByPautaId(pautaId);

        if (!sessao.isFinished()) {
            log.error("Tentativa de consulta ao resultado antes do fim da sessão para a pauta ID: {}", pautaId);
            throw new SessionNotFinishedException("O resultado da votação só pode ser consultado após o encerramento da sessão.");
        }

        List<Voto> votosPorPauta = votoService.findAllByPautaId(pautaId);
        Map<Escolha, Long> contagemDosVotos = votosPorPauta.stream()
                .collect(Collectors.groupingBy(Voto::getEscolha, Collectors.counting()));

        log.info("Contagem de votos para a pauta ID: {}", pautaId);
        return this.create(pauta, contagemDosVotos);
    }
}

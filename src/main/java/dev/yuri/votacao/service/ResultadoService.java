package dev.yuri.votacao.service;

import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Resultado;
import dev.yuri.votacao.model.enums.Escolha;
import dev.yuri.votacao.repository.ResultadoRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ResultadoService {
    private final ResultadoRepository resultadoRepository;

    public ResultadoService(ResultadoRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    public Optional<Resultado> getByPautaId(Long pautaId) {
        return resultadoRepository.findById(pautaId);
    }

    public Resultado create(Pauta pauta, Map<Escolha, Long> contagemDosVotos) {
        Resultado resultado = new Resultado();
        resultado.setPauta(pauta);
        resultado.setQuantidadeSim(contagemDosVotos.getOrDefault(Escolha.SIM, 0L));
        resultado.setQuantidadeNao(contagemDosVotos.getOrDefault(Escolha.NAO, 0L));
        resultado.calcularSituacao();

        return resultadoRepository.save(resultado);
    }
}

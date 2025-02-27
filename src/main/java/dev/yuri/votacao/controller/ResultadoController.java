package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.response.ResultadoResponse;
import dev.yuri.votacao.mapper.ResultadoMapper;
import dev.yuri.votacao.service.ResultadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resultado")
public class ResultadoController {
    private static final Logger log = LoggerFactory.getLogger(ResultadoController.class);

    private final ResultadoService resultadoService;
    private final ResultadoMapper resultadoMapper;

    public ResultadoController(ResultadoService resultadoService, ResultadoMapper resultadoMapper) {
        this.resultadoService = resultadoService;
        this.resultadoMapper = resultadoMapper;
    }

    @GetMapping("/{pautaId}")
    public ResponseEntity<ResultadoResponse> result(@PathVariable Long pautaId) {
        log.info("Buscando resultado para pauta com ID: {}", pautaId);
        var resultado = resultadoService.getResultado(pautaId);
        return ResponseEntity.ok(resultadoMapper.toResponse(resultado));
    }
}

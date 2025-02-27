package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.response.ResultadoResponse;
import dev.yuri.votacao.mapper.ResultadoMapper;
import dev.yuri.votacao.model.Resultado;
import dev.yuri.votacao.service.ResultadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resultado")
public class ResultadoController {
    private final ResultadoService resultadoService;
    private final ResultadoMapper resultadoMapper;

    public ResultadoController(ResultadoService resultadoService, ResultadoMapper resultadoMapper) {
        this.resultadoService = resultadoService;
        this.resultadoMapper = resultadoMapper;
    }

    @GetMapping("/{pautaId}")
    public ResponseEntity<ResultadoResponse> result(@PathVariable Long pautaId) {
        var resultado = resultadoService.getResultado(pautaId);
        return ResponseEntity.ok(resultadoMapper.toResponse(resultado));
    }
}

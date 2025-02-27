package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.response.ResultadoResponse;
import dev.yuri.votacao.mapper.ResultadoMapper;
import dev.yuri.votacao.service.ResultadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resultado")
@Tag(name = "Resultado", description = "Endpoint para consultar resultados de votações")
public class ResultadoController {
    private static final Logger log = LoggerFactory.getLogger(ResultadoController.class);

    private final ResultadoService resultadoService;
    private final ResultadoMapper resultadoMapper;

    public ResultadoController(ResultadoService resultadoService, ResultadoMapper resultadoMapper) {
        this.resultadoService = resultadoService;
        this.resultadoMapper = resultadoMapper;
    }

    @GetMapping("/{pautaId}")
    @Operation(
            summary = "Consultar resultado de uma pauta",
            description = "Retorna o resultado da votação de uma pauta específica pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada ou sem votos registrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<ResultadoResponse> result(@PathVariable Long pautaId) {
        log.info("Buscando resultado para pauta com ID: {}", pautaId);
        var resultado = resultadoService.getResultado(pautaId);
        return ResponseEntity.ok(resultadoMapper.toResponse(resultado));
    }
}

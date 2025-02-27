package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.SessaoDTO;
import dev.yuri.votacao.dto.response.SessaoResponse;
import dev.yuri.votacao.mapper.SessaoMapper;
import dev.yuri.votacao.service.SessaoService;
import dev.yuri.votacao.util.UriUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessao")
@Tag(name = "Sessão", description = "Endpoints para gerenciar sessões de votação")
public class SessaoController {
    private static final Logger log = LoggerFactory.getLogger(SessaoController.class);

    private final SessaoService sessaoService;
    private final SessaoMapper sessaoMapper;

    public SessaoController(SessaoService sessaoService, SessaoMapper sessaoMapper) {
        this.sessaoService = sessaoService;
        this.sessaoMapper = sessaoMapper;
    }

    @PostMapping
    @Transactional
    @Operation(
            summary = "Abrir uma nova sessão de votação",
            description = "Cria e abre uma nova sessão de votação com os dados fornecidos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão criada e aberta com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<SessaoResponse> open(@Valid @RequestBody SessaoDTO sessaoDTO) {
        log.info("Iniciando abertura da sessão: {}", sessaoDTO);
        var sessao = sessaoMapper.toEntity(sessaoDTO);
        var savedSessao = sessaoService.save(sessao);

        log.info("Sessão criada com sucesso: {}", savedSessao);
        return ResponseEntity
                .created(UriUtil.buildUri(savedSessao.getId()))
                .body(sessaoMapper.toResponse(savedSessao));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar sessão por ID",
            description = "Retorna os detalhes de uma sessão de votação pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão encontrada"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<SessaoResponse> getById(@PathVariable Long id) {
        log.info("Buscando sessão com ID: {}", id);
        return ResponseEntity.ok(sessaoMapper.toResponse(sessaoService.findById(id)));
    }

}

package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.SessaoDTO;
import dev.yuri.votacao.dto.response.SessaoResponse;
import dev.yuri.votacao.mapper.SessaoMapper;
import dev.yuri.votacao.service.SessaoService;
import dev.yuri.votacao.util.UriUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessao")
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
    public ResponseEntity<SessaoResponse> getById(@PathVariable Long id) {
        log.info("Buscando sessão com ID: {}", id);
        return ResponseEntity.ok(sessaoMapper.toResponse(sessaoService.findById(id)));
    }

}

package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.SessaoDTO;
import dev.yuri.votacao.dto.response.SessaoResponse;
import dev.yuri.votacao.mapper.SessaoMapper;
import dev.yuri.votacao.service.SessaoService;
import dev.yuri.votacao.util.UriUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessao")
public class SessaoController {
    private final SessaoService sessaoService;
    private final SessaoMapper sessaoMapper;

    public SessaoController(SessaoService sessaoService, SessaoMapper sessaoMapper) {
        this.sessaoService = sessaoService;
        this.sessaoMapper = sessaoMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<SessaoResponse> open(@Valid @RequestBody SessaoDTO sessaoDTO) {
        var sessao = sessaoMapper.toEntity(sessaoDTO);
        var savedSessao = sessaoService.save(sessao);

        return ResponseEntity
                .created(UriUtil.buildUri(savedSessao.getId()))
                .body(sessaoMapper.toResponse(savedSessao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sessaoMapper.toResponse(sessaoService.findById(id)));
    }

}

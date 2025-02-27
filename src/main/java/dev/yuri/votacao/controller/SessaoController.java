package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.SessaoDTO;
import dev.yuri.votacao.dto.response.SessaoResponse;
import dev.yuri.votacao.mapper.SessaoMapper;
import dev.yuri.votacao.service.SessaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedSessao.getId())
                .toUri();

        return ResponseEntity.created(location).body(sessaoMapper.toResponse(savedSessao));
    }

}

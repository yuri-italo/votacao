package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.PautaDTO;
import dev.yuri.votacao.mapper.PautaMapper;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.service.PautaService;
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
@RequestMapping("/api/v1/pauta")
public class PautaController {
    private final PautaService pautaService;
    private final PautaMapper pautaMapper;

    public PautaController(PautaService pautaService, PautaMapper pautaMapper) {
        this.pautaService = pautaService;
        this.pautaMapper = pautaMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Pauta> save(@Valid @RequestBody PautaDTO pautaDTO) {
        var pauta = pautaMapper.toEntity(pautaDTO);
        var savedPauta = pautaService.save(pauta);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPauta.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedPauta);
    }
}

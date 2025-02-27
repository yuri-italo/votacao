package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.VotoDTO;
import dev.yuri.votacao.dto.response.VotoResponse;
import dev.yuri.votacao.mapper.ResultadoMapper;
import dev.yuri.votacao.mapper.VotoMapper;
import dev.yuri.votacao.service.VotoService;
import dev.yuri.votacao.util.UriUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/voto")
public class VotoController {
    private final VotoService votoService;
    private final VotoMapper votoMapper;

    public VotoController(VotoService votoService, VotoMapper votoMapper, ResultadoMapper resultadoMapper) {
        this.votoService = votoService;
        this.votoMapper = votoMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<VotoResponse> vote(@Valid @RequestBody VotoDTO votoDTO) {
        var voto = votoMapper.toEntity(votoDTO);
        var savedVoto = votoService.save(voto);

        return ResponseEntity
                .created(UriUtil.buildUri(savedVoto.getId()))
                .body(votoMapper.toResponse(savedVoto));
    }
}

package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.PautaDTO;
import dev.yuri.votacao.dto.response.PautaResponse;
import dev.yuri.votacao.mapper.PautaMapper;
import dev.yuri.votacao.service.PautaService;
import dev.yuri.votacao.util.UriUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PautaResponse> save(@Valid @RequestBody PautaDTO pautaDTO) {
        var pauta = pautaMapper.toEntity(pautaDTO);
        var savedPauta = pautaService.save(pauta);

        return ResponseEntity
                .created(UriUtil.buildUri(savedPauta.getId()))
                .body(pautaMapper.toResponse(pauta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PautaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pautaMapper.toResponse(pautaService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pautaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

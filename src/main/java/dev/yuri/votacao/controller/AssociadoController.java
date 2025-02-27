package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.AssociadoDTO;
import dev.yuri.votacao.dto.response.AssociadoResponse;
import dev.yuri.votacao.mapper.AssociadoMapper;
import dev.yuri.votacao.service.AssociadoService;
import dev.yuri.votacao.util.UriUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/associado")
public class AssociadoController {
    private final AssociadoService associadoService;
    private final AssociadoMapper associadoMapper;

    public AssociadoController(AssociadoService associadoService, AssociadoMapper associadoMapper) {
        this.associadoService = associadoService;
        this.associadoMapper = associadoMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<AssociadoResponse> save(@Valid @RequestBody AssociadoDTO associadoDTO) {
        var associado = associadoMapper.toEntity(associadoDTO);
        var savedAssociado = associadoService.save(associado);

        return ResponseEntity
                .created(UriUtil.buildUri(savedAssociado.getId()))
                .body(associadoMapper.toResponse(savedAssociado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssociadoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(associadoMapper.toResponse(associadoService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        associadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

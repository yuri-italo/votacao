package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.AssociadoDTO;
import dev.yuri.votacao.mapper.AssociadoMapper;
import dev.yuri.votacao.model.Associado;
import dev.yuri.votacao.service.AssociadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
    public ResponseEntity<Associado> save(@Valid @RequestBody AssociadoDTO associadoDTO) {
        var associado = associadoMapper.toEntity(associadoDTO);
        var savedAssociado = associadoService.save(associado);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAssociado.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedAssociado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        associadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

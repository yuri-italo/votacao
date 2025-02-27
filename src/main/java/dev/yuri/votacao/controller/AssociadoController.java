package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.AssociadoDTO;
import dev.yuri.votacao.dto.response.AssociadoResponse;
import dev.yuri.votacao.mapper.AssociadoMapper;
import dev.yuri.votacao.service.AssociadoService;
import dev.yuri.votacao.util.UriUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/associado")
public class AssociadoController {
    private static final Logger log = LoggerFactory.getLogger(AssociadoController.class);

    private final AssociadoService associadoService;
    private final AssociadoMapper associadoMapper;

    public AssociadoController(AssociadoService associadoService, AssociadoMapper associadoMapper) {
        this.associadoService = associadoService;
        this.associadoMapper = associadoMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<AssociadoResponse> save(@Valid @RequestBody AssociadoDTO associadoDTO) {
        log.info("Iniciando criação de associado: {}", associadoDTO);
        var associado = associadoMapper.toEntity(associadoDTO);
        var savedAssociado = associadoService.save(associado);

        log.info("Associado criado com sucesso: {}", savedAssociado);
        return ResponseEntity
                .created(UriUtil.buildUri(savedAssociado.getId()))
                .body(associadoMapper.toResponse(savedAssociado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssociadoResponse> getById(@PathVariable Long id) {
        log.info("Iniciando busca do associado com ID: {}", id);
        return ResponseEntity.ok(associadoMapper.toResponse(associadoService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Iniciando a remoção do associado com ID: {}", id);
        associadoService.delete(id);
        log.info("Associado com ID: {} removido com sucesso", id);
        return ResponseEntity.noContent().build();
    }

}

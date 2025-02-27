package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.AssociadoDTO;
import dev.yuri.votacao.dto.response.AssociadoResponse;
import dev.yuri.votacao.mapper.AssociadoMapper;
import dev.yuri.votacao.service.AssociadoService;
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
@RequestMapping("/api/v1/associado")
@Tag(name = "Associado", description = "Endpoints para gerenciar associados")
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
    @Operation(summary = "Criar um novo associado", description = "Cria um novo associado com os dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associado criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
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
    @Operation(summary = "Buscar associado por ID", description = "Retorna os detalhes de um associado pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associado encontrado"),
            @ApiResponse(responseCode = "404", description = "Associado não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<AssociadoResponse> getById(@PathVariable Long id) {
        log.info("Iniciando busca do associado com ID: {}", id);
        return ResponseEntity.ok(associadoMapper.toResponse(associadoService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Remover associado por ID", description = "Remove um associado pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Associado removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Associado não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Iniciando a remoção do associado com ID: {}", id);
        associadoService.delete(id);
        log.info("Associado com ID: {} removido com sucesso", id);
        return ResponseEntity.noContent().build();
    }

}

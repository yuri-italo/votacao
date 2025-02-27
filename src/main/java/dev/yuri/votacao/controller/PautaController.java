package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.PautaDTO;
import dev.yuri.votacao.dto.response.PautaResponse;
import dev.yuri.votacao.mapper.PautaMapper;
import dev.yuri.votacao.service.PautaService;
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
@RequestMapping("/api/v1/pauta")
@Tag(name = "Pauta", description = "Endpoints para gerenciar pautas")
public class PautaController {
    private static final Logger log = LoggerFactory.getLogger(PautaController.class);

    private final PautaService pautaService;
    private final PautaMapper pautaMapper;

    public PautaController(PautaService pautaService, PautaMapper pautaMapper) {
        this.pautaService = pautaService;
        this.pautaMapper = pautaMapper;
    }

    @PostMapping
    @Transactional
    @Operation(
            summary = "Criar uma nova pauta",
            description = "Cria uma nova pauta com os dados fornecidos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<PautaResponse> save(@Valid @RequestBody PautaDTO pautaDTO) {
        log.info("Iniciando criação de pauta: {}", pautaDTO);
        var pauta = pautaMapper.toEntity(pautaDTO);
        var savedPauta = pautaService.save(pauta);

        log.info("Pauta criada com sucesso: {}", savedPauta);
        return ResponseEntity
                .created(UriUtil.buildUri(savedPauta.getId()))
                .body(pautaMapper.toResponse(pauta));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pauta por ID",
            description = "Retorna os detalhes de uma pauta pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pauta encontrada"),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<PautaResponse> getById(@PathVariable Long id) {
        log.info("Buscando pauta com ID: {}", id);
        return ResponseEntity.ok(pautaMapper.toResponse(pautaService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Remover pauta por ID",
            description = "Remove uma pauta pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pauta removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Iniciando remoção da pauta com ID: {}", id);
        pautaService.delete(id);
        log.info("Pauta com ID: {} removida com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}
